package com.pickdsm.pickserverspring.domain.teacher.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.spi.QueryApplicationSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.teacher.api.TeacherApi
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainComebackStudentRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryStudentStatusCountResponse
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.TimeQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.UserQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.time.exception.TimeNotFoundException
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate

@UseCase
class TeacherUseCase(
    private val userSpi: UserSpi,
    private val userQueryTeacherSpi: UserQueryTeacherSpi,
    private val statusCommandTeacherSpi: StatusCommandTeacherSpi,
    private val timeQueryTeacherSpi: TimeQueryTeacherSpi,
    private val queryApplicationSpi: QueryApplicationSpi,
    private val queryStatusSpi: QueryStatusSpi,
) : TeacherApi {

    override fun updateStudentStatus(request: DomainUpdateStudentStatusRequest) {
        val teacherId = userSpi.getCurrentUserId()
        val userIdList = request.userList.map { it.userId }
        val userList = userQueryTeacherSpi.queryUserInfo(userIdList)
        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())

        val statusList = request.userList.map {
            val user = userList.find { user -> user.id == it.userId }
                ?: throw UserNotFoundException
            val time = timeList.timeList.find { time -> time.period == request.period }
                ?: throw TimeNotFoundException

            Status(
                studentId = user.id,
                teacherId = teacherId,
                startPeriod = time.period,
                endPeriod = time.period,
                type = it.status,
            )
        }
        statusCommandTeacherSpi.saveAllStatus(statusList)
    }

    override fun getStudentStatusCount(): QueryStudentStatusCountResponse {
        val today = LocalDate.now()
        val status = queryStatusSpi.queryPicnicStudentInfoListByToday(today)
        val picnicCount = status.count()

        val classroomMovement = queryStatusSpi.queryMovementStudentInfoListByToday(today)
        val classroomMovementCount = classroomMovement.count()

        val application = queryApplicationSpi.queryPicnicApplicationListByToday(today)
        val applicationCount = application.count()

        return QueryStudentStatusCountResponse(
            picnic = picnicCount,
            classroomMovement = classroomMovementCount,
            application = applicationCount,
        )
    }

    override fun comebackStudent(request: DomainComebackStudentRequest) {
        val picnicStatusStudent = queryStatusSpi.queryPicnicStudentByStudentId(request.studentId)
            ?: throw StatusNotFoundException

        statusCommandTeacherSpi.saveStatus(
            picnicStatusStudent.changeStatusToAttendance(
                endPeriod = request.endPeriod,
            )
        )
    }
}
