package com.pickdsm.pickserverspring.domain.admin.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.admin.api.AdminApi
import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentAttendanceList
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentAttendanceList.StudentElement
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentListByGradeAndClassNum
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentListByGradeAndClassNum.StudentElementByGradeAndClassNum
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryTypeResponse
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.exception.CannotChangeEmploymentException
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.schedule.spi.QueryScheduleSpi
import com.pickdsm.pickserverspring.domain.schedule.spi.exception.HomecomingDayException
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.Type
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.TypeNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.CommandTypeSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QueryTypeSpi
import com.pickdsm.pickserverspring.domain.teacher.exception.TeacherNotFoundException
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.TimeQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.time.exception.TimeNotFoundException
import com.pickdsm.pickserverspring.domain.time.spi.QueryTimeSpi
import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.dto.request.UserInfoRequest
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
    private val commandTypeSpi: CommandTypeSpi,
    private val queryScheduleSpi: QueryScheduleSpi,
) : AdminApi {

    override fun updateStudentStatusOfClass(request: DomainUpdateStudentStatusOfClassRequest) {
        val teacherId = userSpi.getCurrentUserId()
        val statusList = queryStatusSpi.queryStatusListByToday()
        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())
        val nowPeriod = queryTimeSpi.queryNowPeriod(timeList)
        val userIdList = request.userList.map { it.userId }
        val userIdRequest = UserInfoRequest(userIdList)
        val userInfoList = userSpi.queryUserInfo(userIdRequest)

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

                StatusType.FIELD_TRIP_START, StatusType.HOME, StatusType.DROPOUT -> {
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
        if (queryScheduleSpi.queryIsHomecomingDay(date.toString())) {
            throw HomecomingDayException
        }

        val dateType = queryTypeSpi.queryDirectorTypeByDate(date) ?: DirectorType.SELF_STUDY
        val dateStatusList = queryStatusSpi.queryStatusListByDate(date)
        val classroom = queryClassroomSpi.queryClassroomById(classroomId)
            ?: throw ClassroomNotFoundException
        val startPeriod = if (dateType == DirectorType.FRI_CLUB) 6 else 8
        val students = mutableListOf<StudentElement>()

        when (dateType) {
            DirectorType.AFTER_SCHOOL -> {
                val afterSchoolList = queryAfterSchoolSpi.queryAfterSchoolListByClassroomId(classroomId)
                val afterSchoolStudentIdList = afterSchoolList.map { it.studentId }
                val userIdRequest = UserInfoRequest(afterSchoolStudentIdList)
                val afterSchoolUserInfos = userSpi.queryUserInfo(userIdRequest)

                afterSchoolUserInfos.map { user ->
                    val afterSchoolStatusList = dateStatusList.filter { it.studentId == user.id }
                    val afterSchoolStatusTypes = mutableListOf<StatusType>()

                    for (i in startPeriod..10) {
                        val statusType = afterSchoolStatusList.getStatusTypeByUserIdAndBetweenStartPeriodAndEndPeriod(
                            userId = user.id,
                            statusPeriod = i,
                        )
                        statusType.awaitOrPicnicRejectChangeToAttendance(afterSchoolStatusTypes)
                    }

                    students.add(user.buildStudentElement(afterSchoolStatusTypes))
                }
            }

            DirectorType.FRI_CLUB, DirectorType.TUE_CLUB -> {
                val clubList = queryClubSpi.queryClubListByClassroomId(classroomId)
                val clubStudentIdList = clubList.map { it.studentId }
                val userIdRequest = UserInfoRequest(clubStudentIdList)
                val clubUserInfos = userSpi.queryUserInfo(userIdRequest)

                clubUserInfos.map { user ->
                    val clubStatusList = dateStatusList.filter { it.studentId == user.id }
                    val clubStatusTypes = mutableListOf<StatusType>()

                    for (i in startPeriod..10) {
                        val statusType = clubStatusList.getStatusTypeByUserIdAndBetweenStartPeriodAndEndPeriod(
                            userId = user.id,
                            statusPeriod = i,
                        )
                        statusType.awaitOrPicnicRejectChangeToAttendance(clubStatusTypes)
                    }

                    students.add(user.buildStudentElement(clubStatusTypes))
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
                        val statusType = classroomStatusList.getStatusTypeByUserIdAndBetweenStartPeriodAndEndPeriod(
                            userId = user.id,
                            statusPeriod = i,
                        )
                        statusType.awaitOrPicnicRejectChangeToAttendance(classroomStatusTypes)
                    }

                    val classroomElement = StudentElement(
                        studentId = user.id,
                        studentNumber = user.num,
                        studentName = user.name,
                        typeList = classroomStatusTypes,
                    )
                    students.add(classroomElement)
                }
            }
        }

        return QueryStudentAttendanceList(
            classroom = classroom.name,
            studentList = students.sortedBy { it.studentNumber },
        )
    }

    private fun StatusType.awaitOrPicnicRejectChangeToAttendance(statusTypes: MutableList<StatusType>) {
        val isTypeAwaitOrPicnicReject = this == StatusType.AWAIT || this == StatusType.PICNIC_REJECT
        if (isTypeAwaitOrPicnicReject) {
            statusTypes.add(StatusType.ATTENDANCE)
        } else {
            statusTypes.add(this)
        }
    }

    private fun User.paddedUserNum() = this.num.toString().padStart(2, '0')

    private fun User.buildStudentElement(statusTypeList: MutableList<StatusType>) =
        StudentElement(
            studentId = this.id,
            studentNumber = "${this.grade}${this.classNum}${this.paddedUserNum()}",
            studentName = this.name,
            typeList = statusTypeList,
        )

    override fun getTypeByDate(date: LocalDate): QueryTypeResponse {
        val type = queryTypeSpi.queryTypeByDate(date)
            ?: throw StatusNotFoundException

        return QueryTypeResponse(
            id = type.id,
            date = type.date,
            type = type.type,
        )
    }

    override fun getStudentStatusListByGradeAndClassNum(
        grade: Int?,
        classNum: Int?,
    ): QueryStudentListByGradeAndClassNum {
        val classroomTeacherId = queryClassroomSpi.queryClassroomByGradeAndClassNum(
            grade = grade,
            classNum = classNum,
        )?.homeroomTeacherId ?: throw TeacherNotFoundException
        val homeroomTeacherInfo = userSpi.queryUserInfoByUserId(classroomTeacherId)

        val studentInfos = userSpi.queryUserInfoByGradeAndClassNum(
            grade = grade,
            classNum = classNum,
        )

        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())
        val nowPeriod = queryTimeSpi.queryNowPeriod(timeList)
        val statusList = queryStatusSpi.queryStatusListByDate(LocalDate.now())

        val studentList = studentInfos.map {
            val user = studentInfos.find { studentInfo -> studentInfo.id == it.id }
                ?: throw UserNotFoundException
            val statusType = statusList.getStatusTypeByUserIdAndBetweenStartPeriodAndEndPeriod(
                userId = user.id,
                statusPeriod = nowPeriod,
            )
            StudentElementByGradeAndClassNum(
                studentId = user.id,
                studentNumber = user.num.toInt(),
                studentName = user.name,
                status = statusType,
            )
        }

        return QueryStudentListByGradeAndClassNum(
            teacherName = homeroomTeacherInfo.name,
            studentList = studentList,
        )
    }

    private fun List<Status>.getStatusTypeByUserIdAndBetweenStartPeriodAndEndPeriod(userId: UUID, statusPeriod: Int) =
        this.find { status ->
            status.studentId == userId && status.startPeriod <= statusPeriod && status.endPeriod >= statusPeriod
        }?.type ?: StatusType.ATTENDANCE

    override fun saveOrUpdateType(date: LocalDate, type: DirectorType) {
        val existType = queryTypeSpi.queryTypeByDate(date)

        existType?.let {
            commandTypeSpi.saveType(
                it.changeType(type)
            )
        } ?: commandTypeSpi.saveType(
                Type(
                    date = date,
                    type = type,
                ),
            )
    }
}
