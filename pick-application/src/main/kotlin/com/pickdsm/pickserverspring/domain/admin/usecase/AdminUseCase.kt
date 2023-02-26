package com.pickdsm.pickserverspring.domain.admin.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.admin.api.AdminApi
import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentAttendanceList
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentAttendanceList.StudentElement
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
import java.util.*

@UseCase
class AdminUseCase(
    private val userSpi: UserSpi,
    private val statusCommandTeacherSpi: StatusCommandTeacherSpi,
    private val timeQueryTeacherSpi: TimeQueryTeacherSpi,
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

                StatusType.FIELD_TRIP_START, StatusType.LEAVE -> {
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
        val dateType = queryTypeSpi.queryTypeByDate(date)
        val timeList = timeQueryTeacherSpi.queryTime(date)
        val classroom = queryClassroomSpi.queryClassroomById(classroomId)
        val students = mutableListOf<StudentElement>()

        when (DirectorType.CLUB) { // TODO: type 바꾸기 우선 테스트 위해서 이렇게 함
            DirectorType.CLUB -> {
                val studentStatusList = queryStatusSpi.queryStudentStatusByDateAndOrderByStartPeriod(date)
                val club = queryClubSpi.queryClubListByClassroomId(classroomId)
                val studentIdList = club.map { it.studentId }
                val userInfos = userSpi.queryUserInfo(studentIdList)
                val typeList = studentStatusList
                    .map { status ->
                        status.type
                    }
                    .map { statusType ->
                        studentStatusList.find { statusType != StatusType.PICNIC_REJECT && statusType != StatusType.AWAIT }
                            ?.type ?: StatusType.ATTENDANCE
                    }

                studentStatusList.map {
                    val user = userInfos.find { user -> user.id == it.studentId }
                        ?: throw UserNotFoundException
                    val studentElement = StudentElement(
                        studentId = user.id,
                        studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                        studentName = user.name,
                        typeList = typeList,
                    )
                    students.add(studentElement)
                }
            }

            else -> throw TypeNotFoundException
        }

        return QueryStudentAttendanceList(
            classroom = classroom.name,
            studentList = students.distinct(),
        )
    }

    private fun checkUserNumLessThanTen(userNum: Int) =
        if (userNum < 10) {
            "0$userNum"
        } else {
            userNum.toString()
        }
}
