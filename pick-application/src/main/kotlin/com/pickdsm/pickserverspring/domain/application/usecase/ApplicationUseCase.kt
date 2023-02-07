package com.pickdsm.pickserverspring.domain.application.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.api.ApplicationApi
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationElement
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicStudentElement
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicStudentList
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryStudentStatusElement
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryStudentStatusList
import com.pickdsm.pickserverspring.domain.application.exception.ApplicationNotFoundException
import com.pickdsm.pickserverspring.domain.application.spi.CommandApplicationSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryApplicationSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.application.spi.UserQueryApplicationSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomMovementSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate
import java.util.*

@UseCase
class ApplicationUseCase(
    private val commandApplicationSpi: CommandApplicationSpi,
    private val queryApplicationSpi: QueryApplicationSpi,
    private val statusCommandTeacherSpi: StatusCommandTeacherSpi,
    private val queryStatusSpi: QueryStatusSpi,
    private val userQueryApplicationSpi: UserQueryApplicationSpi,
    private val userSpi: UserSpi,
    private val queryClassroomMovementSpi: QueryClassroomMovementSpi,
    private val queryClassroomSpi: QueryClassroomSpi,
) : ApplicationApi {

    companion object {
        const val ALL = "all"
        const val MOVEMENT = "movement"
    }

    override fun saveApplicationToGoOut(request: DomainApplicationGoOutRequest) {
        val studentId = userSpi.getCurrentUserId()
        val status = Status(
            studentId = studentId,
            teacherId = UUID(0, 0), // TODO: 선생님 아이디 뭐로 넣을지 나중에 정하기
            type = StatusType.AWAIT,
            date = LocalDate.now(),
            startTime = request.desiredStartTime,
            endTime = request.desiredEndTime,
        )
        val saveStatusId = statusCommandTeacherSpi.saveStatusAndGetStatusId(status)
        val application = Application(
            statusId = saveStatusId,
            desiredStartTime = request.desiredStartTime,
            desiredEndTime = request.desiredEndTime,
            reason = request.reason,
        )
        commandApplicationSpi.saveApplication(application)
    }

    override fun queryPicnicApplicationListByGradeAndClassNum(
        grade: String,
        classNum: String,
    ): QueryPicnicApplicationList {
        val todayOutingList = queryApplicationSpi.queryPicnicApplicationListByToday(LocalDate.now())
        val todayStatusList = queryStatusSpi.queryAwaitStudentListByToday(LocalDate.now())

        val todayApplicationStudentIdList = todayStatusList.map { it.studentId }

        val userList = userQueryApplicationSpi.queryUserInfo(todayApplicationStudentIdList)

        val outing: List<QueryPicnicApplicationElement> = todayStatusList
            .filter { status ->
                val user = userList.find { user -> user.id == status.studentId } ?: return@filter false

                val studentGrade = grade.toIntOrNull() ?: 0
                val studentClassNum = classNum.toIntOrNull() ?: 0

                when {
                    studentGrade != 0 && studentClassNum == 0 -> studentGrade == user.grade
                    studentGrade == 0 -> false
                    else -> (studentGrade == user.grade && studentClassNum == user.classNum)
                }
            }
            .map { status ->
                val user = userList.find { user -> user.id == status.studentId }
                    ?: throw UserNotFoundException

                val application = todayOutingList.find { application -> application.statusId == status.id }
                    ?: throw ApplicationNotFoundException

                val studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}"

                val studentName = user.name

                QueryPicnicApplicationElement(
                    studentId = user.id,
                    studentNumber = studentNumber,
                    studentName = studentName,
                    startTime = application.desiredStartTime,
                    endTime = application.desiredEndTime,
                    reason = application.reason,
                )
            }

        return QueryPicnicApplicationList(outing)
    }

    override fun queryPicnicStudentListByToday(): QueryPicnicStudentList {
        val todayPicnicStudentInfoList = queryStatusSpi.queryPicnicStudentInfoListByToday(LocalDate.now())

        val todayPicnicStudentIdList = todayPicnicStudentInfoList.map { status -> status.studentId }

        val userList = userQueryApplicationSpi.queryUserInfo(todayPicnicStudentIdList)

        val outing: List<QueryPicnicStudentElement> = todayPicnicStudentInfoList
            .map { status ->
                val user = userList.find { user -> user.id == status.studentId }
                    ?: throw UserNotFoundException

                val studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}"

                val studentName = user.name

                QueryPicnicStudentElement(
                    studentId = user.id,
                    studentNumber = studentNumber,
                    studentName = studentName,
                    endTime = status.endTime,
                )
            }

        return QueryPicnicStudentList(outing)
    }

    override fun queryAllStudentStatusByClassroomAndType(classroomId: UUID, type: String): QueryStudentStatusList {
        val now = LocalDate.now()
        val todayMovementStudentInfoList = queryStatusSpi.queryMovementStudentInfoListByToday(now)
        val todayStudentStatus = queryStatusSpi.queryStudentInfoByToday(now)
        val todayMovementStudentIdList = todayMovementStudentInfoList.map { movement -> movement.studentId }
        val userList = userQueryApplicationSpi.queryUserInfo(todayMovementStudentIdList)
        val students = emptyList<QueryStudentStatusElement>()
        val inputClassroom = queryClassroomSpi.queryClassroomById(classroomId)

        when (type) {
            ALL -> {
                todayStudentStatus
                    .map { status ->
                        val user = userList.find { user -> user.id == status.studentId }
                            ?: throw UserNotFoundException
                        if (user.grade == inputClassroom.grade && user.num == inputClassroom.classNum) {
                            val studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}"
                            val studentName = user.name
                            val studentStatus = QueryStudentStatusElement(
                                studentId = user.id,
                                studentNumber = studentNumber,
                                studentName = studentName,
                                type = status.type,
                                classroomName = "",
                            )
                            students.plus(studentStatus)
                        }
                    }
            }
            MOVEMENT -> {
                todayMovementStudentInfoList
                    .map { status ->
                        val user = userList.find { user -> user.id == status.studentId }
                            ?: throw UserNotFoundException
                        if (user.grade == inputClassroom.grade && user.num == inputClassroom.classNum) {
                            val studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}"
                            val studentName = user.name
                            val classroomMovement = queryClassroomMovementSpi.queryClassroomMovementByStatus(status)
                            val classroom = queryClassroomSpi.queryClassroomById(classroomMovement.classroomId)
                            val studentStatus = QueryStudentStatusElement(
                                studentId = user.id,
                                studentNumber = studentNumber,
                                studentName = studentName,
                                type = status.type,
                                classroomName = classroom.name,
                            )
                            students.plus(studentStatus)
                        }
                    }
            }
        }
        return QueryStudentStatusList(
            students = students,
        )
    }

    private fun checkUserNumLessThanTen(userNum: Int) =
        if (userNum < 10) {
            "0$userNum"
        } else {
            userNum.toString()
        }
}
