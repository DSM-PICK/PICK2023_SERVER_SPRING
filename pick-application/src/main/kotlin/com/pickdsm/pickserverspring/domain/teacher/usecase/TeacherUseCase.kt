package com.pickdsm.pickserverspring.domain.teacher.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.exception.FloorNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.teacher.api.TeacherApi
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainComebackStudentRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryMovementStudentList
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryMovementStudentList.MovementStudent
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryStudentStatusCountResponse
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.TimeQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.time.exception.TimeNotFoundException
import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.dto.request.UserInfoRequest
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate
import java.util.UUID

@UseCase
class TeacherUseCase(
    private val userSpi: UserSpi,
    private val statusCommandTeacherSpi: StatusCommandTeacherSpi,
    private val timeQueryTeacherSpi: TimeQueryTeacherSpi,
    private val queryClassroomSpi: QueryClassroomSpi,
    private val querySelfStudyDirectorSpi: QuerySelfStudyDirectorSpi,
    private val queryStatusSpi: QueryStatusSpi,
) : TeacherApi {

    override fun updateStudentStatus(request: DomainUpdateStudentStatusRequest) {
        val teacherId = userSpi.getCurrentUserId()
        val userInfo = userSpi.queryUserInfoByUserId(request.userId)
        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())
        val time = timeList.timeList.find { time -> time.period == request.period }
            ?: throw TimeNotFoundException

        val status = queryStatusSpi.queryStatusByStudentIdAndStartPeriodAndEndPeriodAndToday(
            studentId = userInfo.id,
            startPeriod = time.period,
            endPeriod = time.period,
        )

        val saveOrUpdateStatus = status?.changeStudentStatus(request.status)
            ?: Status(
                studentId = userInfo.id,
                teacherId = teacherId,
                startPeriod = time.period,
                endPeriod = time.period,
                type = request.status,
            )
        statusCommandTeacherSpi.saveStatus(saveOrUpdateStatus)
    }

    override fun getStudentStatusCount(): QueryStudentStatusCountResponse {
        val teacherResponsibleFloor = querySelfStudyDirectorSpi.queryResponsibleFloorByTeacherId(userSpi.getCurrentUserId())
            ?: throw FloorNotFoundException

        return QueryStudentStatusCountResponse(
            picnic = queryStatusSpi.queryPicnicStatusSizeByToday(),
            application = queryStatusSpi.queryPicnicApplicationStatusSizeByToday(),
            classroomMovement = queryStatusSpi.queryMovementStatusSizeByFloorAndToday(teacherResponsibleFloor),
        )
    }

    override fun comebackStudent(request: DomainComebackStudentRequest) {
        val picnicStudent = queryStatusSpi.queryPicnicStudentByStudentIdAndToday(request.studentId)
            ?: throw StatusNotFoundException
        statusCommandTeacherSpi.saveStatus(
            picnicStudent.changeStatusToAttendance(
                endPeriod = request.endPeriod,
            ),
        )
    }

    override fun getMovementStudents(classroomId: UUID): QueryMovementStudentList {
        val classroom = queryClassroomSpi.queryClassroomById(classroomId)
            ?: throw ClassroomNotFoundException
        val movementStatusList = queryStatusSpi.queryMovementStatusListByTodayAndClassroomId(classroomId)
        val movementStudentIdList = movementStatusList.map { it.studentId }
        val userIdRequest = UserInfoRequest(movementStudentIdList)
        val movementUserInfos = userSpi.queryUserInfo(userIdRequest)

        val movementList = movementStatusList.map {
            val user = movementUserInfos.find { user -> user.id == it.studentId }
                ?: throw UserNotFoundException

            MovementStudent(
                studentId = user.id,
                studentNumber = "${user.grade}${user.classNum}${user.paddedUserNum()}",
                studentName = user.name,
                type = it.type.name,
                classroomName = classroom.name,
            )
        }

        return QueryMovementStudentList(movementList.sortedBy { it.studentNumber })
    }

    private fun User.paddedUserNum(): String =
        this.num.toString().padStart(2, '0')
}
