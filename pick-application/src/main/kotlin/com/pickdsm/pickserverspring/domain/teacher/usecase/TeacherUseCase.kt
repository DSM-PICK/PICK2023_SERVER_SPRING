package com.pickdsm.pickserverspring.domain.teacher.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.exception.ApplicationNotFoundException
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.spi.CommandApplicationSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryApplicationSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.teacher.api.TeacherApi
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainComebackStudentRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryMovementStudentList
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryMovementStudentList.MovementStudent
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryMyBuckGradeAndClassNumResponse
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
    private val commandApplicationSpi: CommandApplicationSpi,
    private val queryApplicationSpi: QueryApplicationSpi,
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

    override fun queryStudentStatusCount(): QueryStudentStatusCountResponse {
        val teacherResponsibleFloor = querySelfStudyDirectorSpi.queryResponsibleFloorByTeacherIdAndToday(userSpi.getCurrentUserId())
        return QueryStudentStatusCountResponse(
            picnic = queryStatusSpi.queryPicnicStatusSizeByToday(),
            application = queryStatusSpi.queryPicnicApplicationStatusSizeByToday(),
            classroomMovement = queryStatusSpi.queryMovementStatusSizeByFloorAndToday(teacherResponsibleFloor ?: 2),
        )
    }

    override fun comebackStudent(request: DomainComebackStudentRequest) {
        val picnicStudent = queryStatusSpi.queryPicnicStudentByStudentIdAndToday(request.studentId)
            ?: throw StatusNotFoundException

        val picnicApplication = queryApplicationSpi.queryApplicationByStudentIdAndStatusId(picnicStudent.studentId, picnicStudent.id)
            ?: throw ApplicationNotFoundException

        commandApplicationSpi.saveApplication(
            picnicApplication.changeStatusToAttendance(
                isReturn = true,
            ),
        )

        statusCommandTeacherSpi.saveStatus(
            picnicStudent.changeStatusToAttendance(
                endPeriod = request.endPeriod,
            ),
        )
    }

    override fun queryMovementStudents(classroomId: UUID): QueryMovementStudentList {
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

    override fun queryMyBuckGradeAndClassNum(): QueryMyBuckGradeAndClassNumResponse {
        val userInfo = userSpi.queryUserInfoByUserId(userSpi.getCurrentUserId())

        var userGrade = 1
        var userClassNum = 1

        println(userInfo.grade == 0 && userInfo.classNum == 0)
        if (!(userInfo.grade == 0 && userInfo.classNum == 0)) {
            userGrade = userInfo.grade
            userClassNum = userInfo.classNum
        }

        println(userGrade)
        println(userInfo)

        return QueryMyBuckGradeAndClassNumResponse(userGrade, userClassNum)
    }

    private fun User.paddedUserNum(): String =
        this.num.toString().padStart(2, '0')
}
