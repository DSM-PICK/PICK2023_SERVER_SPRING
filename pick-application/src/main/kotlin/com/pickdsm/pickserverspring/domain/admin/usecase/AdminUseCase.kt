package com.pickdsm.pickserverspring.domain.admin.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.admin.api.AdminApi
import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentAttendanceList
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentAttendanceList.StudentElement
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.exception.CannotChangeEmploymentException
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.TypeNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QueryTypeSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.TimeQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.time.exception.TimeNotFoundException
import com.pickdsm.pickserverspring.domain.time.spi.QueryTimeSpi
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate
import java.util.UUID

@UseCase
class AdminUseCase(
    private val userSpi: UserSpi,
    private val statusCommandTeacherSpi: StatusCommandTeacherSpi,
    private val timeQueryTeacherSpi: TimeQueryTeacherSpi,
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
    private val queryClassroomSpi: QueryClassroomSpi,
    private val queryClubSpi: QueryClubSpi,
    private val queryTypeSpi: QueryTypeSpi,
    private val queryTimeSpi: QueryTimeSpi,
    private val queryStatusSpi: QueryStatusSpi,
) : AdminApi {

    override fun updateStudentStatusOfClass(request: DomainUpdateStudentStatusOfClassRequest) {
        val teacherId = userSpi.getCurrentUserId()
        val statusList = queryStatusSpi.queryStatusListByToday()
        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())
        val nowPeriod = queryTimeSpi.queryNowPeriod(timeList)
        val userIdList = request.userList.map { it.userId }
        val userInfoList = userSpi.queryUserInfo(userIdList)

        val changeStatusList: List<Status> = request.userList.map {
            val user = userInfoList.find { user -> user.id == it.userId }
                ?: throw UserNotFoundException
            val status = statusList.find { status -> status.studentId == it.userId }
                ?: throw StatusNotFoundException
            val time = timeList.timeList.find { time -> time.period == nowPeriod }
                ?: throw TimeNotFoundException

            when (it.status) {
                StatusType.ATTENDANCE -> {
                    status.changeStatusOfClass(
                        teacherId = teacherId,
                        startPeriod = time.period,
                        endPeriod = time.period,
                        type = it.status,
                    )
                }

                StatusType.EMPLOYMENT_START -> {
                    if (user.grade != 3) {
                        throw CannotChangeEmploymentException
                    }
                    status.changeStatusOfClass(
                        teacherId = teacherId,
                        startPeriod = 1,
                        endPeriod = 10,
                        type = it.status,
                    )
                }

                StatusType.FIELD_TRIP_START, StatusType.HOME -> {
                    status.changeStatusOfClass(
                        teacherId = teacherId,
                        startPeriod = time.period,
                        endPeriod = 10,
                        type = it.status,
                    )
                }

                else -> throw TypeNotFoundException
            }
        }
        statusCommandTeacherSpi.saveAllStatus(changeStatusList)
    }

    override fun getStudentAttendanceList(classroomId: UUID, date: LocalDate): QueryStudentAttendanceList {
        val dateType = queryTypeSpi.queryDirectorTypeByDate(date) ?: DirectorType.SELF_STUDY
        val dateStatusList = queryStatusSpi.queryStatusListByDate(date)
        val timeList = timeQueryTeacherSpi.queryTime(date)
        val classroom = queryClassroomSpi.queryClassroomById(classroomId)
        val startPeriod = timeList.timeList.firstOrNull { it.periodType == dateType.name }?.period ?: 8
        val students = mutableListOf<StudentElement>()

        when (dateType) {
            DirectorType.AFTER_SCHOOL -> {
                val afterSchoolList = queryAfterSchoolSpi.queryAfterSchoolListByClassroomId(classroomId)
                val afterSchoolStudentIdList = afterSchoolList.map { it.studentId }
                val afterSchoolUserInfos = userSpi.queryUserInfo(afterSchoolStudentIdList)

                afterSchoolUserInfos.map { user ->
                    val afterSchoolStatusList = dateStatusList.filter { it.studentId == user.id }
                    val afterSchoolStatusTypes = mutableListOf<StatusType>()

                    for (i in startPeriod..10) {
                        val afterSchoolStatus = getStatusByStartPeriodAndEndPeriod(
                            statusList = afterSchoolStatusList,
                            statusPeriod = i,
                            userId = user.id,
                        )

                        awaitOrPicnicRejectChangeToAttendance(
                            statusType = afterSchoolStatus,
                            statusTypes = afterSchoolStatusTypes,
                        )
                    }

                    val afterSchoolElement = StudentElement(
                        studentId = user.id,
                        studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                        studentName = user.name,
                        typeList = afterSchoolStatusTypes,
                    )
                    students.add(afterSchoolElement)
                }
            }

            DirectorType.CLUB -> {
                val clubList = queryClubSpi.queryClubListByClassroomId(classroomId)
                val clubStudentIdList = clubList.map { it.studentId }
                val clubUserInfos = userSpi.queryUserInfo(clubStudentIdList)

                clubUserInfos.map { user ->
                    val clubStatusList = dateStatusList.filter { it.studentId == user.id }
                    val clubStatusTypes = mutableListOf<StatusType>()

                    for (i in startPeriod..10) {
                        val clubStatus = getStatusByStartPeriodAndEndPeriod(
                            statusList = clubStatusList,
                            statusPeriod = i,
                            userId = user.id,
                        )

                        awaitOrPicnicRejectChangeToAttendance(
                            statusType = clubStatus,
                            statusTypes = clubStatusTypes,
                        )
                    }

                    val clubElement = StudentElement(
                        studentId = user.id,
                        studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                        studentName = user.name,
                        typeList = clubStatusTypes,
                    )
                    students.add(clubElement)
                }
            }

            DirectorType.SELF_STUDY -> {
                val classroomUserInfos = userSpi.queryUserInfoByGradeAndClassNum(
                    grade = classroom.grade,
                    classNum = classroom.classNum,
                )

                classroomUserInfos.map { user ->
                    val classroomStatusList = dateStatusList.filter { it.studentId == user.id }
                    val classroomStatusTypes = mutableListOf<StatusType>()

                    for (i in startPeriod..10) {
                        val classroomStatus = getStatusByStartPeriodAndEndPeriod(
                            statusList = classroomStatusList,
                            statusPeriod = i,
                            userId = user.id,
                        )

                        awaitOrPicnicRejectChangeToAttendance(
                            statusType = classroomStatus,
                            statusTypes = classroomStatusTypes,
                        )
                    }

                    val classroomElement = StudentElement(
                        studentId = user.id,
                        studentNumber = user.num.toString(),
                        studentName = user.name,
                        typeList = classroomStatusTypes,
                    )
                    students.add(classroomElement)
                }
            }
        }

        return QueryStudentAttendanceList(
            classroom = classroom.name,
            studentList = students,
        )
    }

    private fun getStatusByStartPeriodAndEndPeriod(
        statusList: List<Status>,
        statusPeriod: Int,
        userId: UUID,
    ): StatusType =
        statusList.find {
            it.startPeriod <= statusPeriod && it.endPeriod >= statusPeriod && it.studentId == userId
        }?.type ?: StatusType.ATTENDANCE

    private fun awaitOrPicnicRejectChangeToAttendance(
        statusType: StatusType,
        statusTypes: MutableList<StatusType>,
    ) {
        if (statusType == StatusType.AWAIT || statusType == StatusType.PICNIC_REJECT) {
            statusTypes.add(StatusType.ATTENDANCE)
        } else {
            statusTypes.add(statusType)
        }
    }

    private fun checkUserNumLessThanTen(userNum: Int) =
        if (userNum < 10) {
            "0$userNum"
        } else {
            userNum.toString()
        }
}
