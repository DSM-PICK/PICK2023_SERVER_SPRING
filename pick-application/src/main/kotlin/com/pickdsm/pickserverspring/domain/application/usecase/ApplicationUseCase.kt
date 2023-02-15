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
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomMovementSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.TimeQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.time.exception.TimeNotFoundException
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate
import java.util.UUID

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
    private val timeQueryTeacherSpi: TimeQueryTeacherSpi,
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
            startPeriod = request.desiredStartPeriod,
            endPeriod = request.desiredEndPeriod,
            type = StatusType.AWAIT,
        )
        val saveStatusId = statusCommandTeacherSpi.saveStatusAndGetStatusId(status)
        commandApplicationSpi.saveApplication(
            Application(
                reason = request.reason,
                statusId = saveStatusId,
            ),
        )
    }

    override fun queryPicnicApplicationListByGradeAndClassNum(
        grade: String,
        classNum: String,
    ): QueryPicnicApplicationList {
        val today = LocalDate.now()
        val todayOutingList = queryApplicationSpi.queryPicnicApplicationListByToday(today)
        val todayStatusList = queryStatusSpi.queryAwaitStudentListByToday(today)
        val todayApplicationStudentIdList = todayStatusList.map { it.studentId }
        val timeList = timeQueryTeacherSpi.queryTime(today)
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
                val startTime = timeList.timeList.find { time -> time.period == status.startPeriod }
                    ?: throw TimeNotFoundException
                val endTime = timeList.timeList.find { time -> time.period == status.endPeriod }
                    ?: throw TimeNotFoundException
                val application = todayOutingList.find { application -> application.statusId == status.id }
                    ?: throw ApplicationNotFoundException

                QueryPicnicApplicationElement(
                    studentId = user.id,
                    studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                    studentName = user.name,
                    startTime = startTime.startTime,
                    endTime = endTime.endTime,
                    reason = application.reason,
                )
            }
            .sortedWith(
                compareBy(QueryPicnicApplicationElement::startTime)
                    .thenBy(QueryPicnicApplicationElement::endTime)
                    .thenBy(QueryPicnicApplicationElement::studentNumber),
            )

        return QueryPicnicApplicationList(outing)
    }

    override fun queryPicnicStudentListByToday(): QueryPicnicStudentList {
        val todayPicnicStudentInfoList = queryStatusSpi.queryPicnicStudentInfoListByToday(LocalDate.now())
        val todayPicnicStudentIdList = todayPicnicStudentInfoList.map { status -> status.studentId }
        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())
        val userList = userQueryApplicationSpi.queryUserInfo(todayPicnicStudentIdList)

        val outing: List<QueryPicnicStudentElement> = todayPicnicStudentInfoList
            .map { status ->
                val user = userList.find { user -> user.id == status.studentId }
                    ?: throw UserNotFoundException
                val endTime = timeList.timeList.find { time -> time.period == status.endPeriod }
                    ?: throw TimeNotFoundException

                QueryPicnicStudentElement(
                    studentId = user.id,
                    studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                    studentName = user.name,
                    endTime = endTime.endTime,
                )
            }
            .sortedWith(
                compareBy(QueryPicnicStudentElement::endTime)
                    .thenBy(QueryPicnicStudentElement::studentNumber),
            )

        return QueryPicnicStudentList(outing)
    }

    override fun queryAllStudentStatusByClassroomAndType(classroomId: UUID, type: String): QueryStudentStatusList {
        val now = LocalDate.now()
        val todayStudentStatusList = queryStatusSpi.queryStudentInfoByToday()
        val classroom = queryClassroomSpi.queryClassroomById(classroomId)
        val grade = classroom.grade ?: throw ClassroomNotFoundException // TODO grade랑 classNum으로 학생 리스트를 가져와서 에러처리했습니다.
        val classNum = classroom.classNum ?: throw ClassroomNotFoundException // TODO 우선 에러처리

        val classroomStudentList = userSpi.queryUserInfoByGradeAndClassNum(grade, classNum)

        val todayMovementStudentInfoList = queryStatusSpi.queryMovementStudentInfoListByToday(now)
        val todayMovementStudentIdList = todayMovementStudentInfoList.map { movement -> movement.studentId }
        val userList = userQueryApplicationSpi.queryUserInfo(todayMovementStudentIdList)

        val students = mutableListOf<QueryStudentStatusElement>()

        when (type) {
            ALL -> {
                classroomStudentList
                    .map { user ->
                        val status = todayStudentStatusList.find { user.id == it.studentId }
                        val studentNumber = "${classroom.grade}${classroom.classNum}${checkUserNumLessThanTen(user.num)}"
                        val studentName = user.name
                        val movementClassroomName = movementStudent(status)
                        val studentStatus = QueryStudentStatusElement(
                            studentId = user.id,
                            studentNumber = studentNumber,
                            studentName = studentName,
                            type = status?.type?.name ?: "",
                            classroomName = movementClassroomName,
                        )
                        students.add(studentStatus)
                    }
            }

            MOVEMENT -> {
                userList
                    .map { user ->
                        if (user.grade == classroom.grade && user.classNum == classroom.classNum) {
                            val studentNumber = "${classroom.grade}${classroom.classNum}${checkUserNumLessThanTen(user.num)}"
                            val status = todayStudentStatusList.find { user.id == it.studentId }
                            val studentName = user.name
                            val movementClassroomName = movementStudent(status)
                            val studentStatus = QueryStudentStatusElement(
                                studentId = user.id,
                                studentNumber = studentNumber,
                                studentName = studentName,
                                type = status?.type?.name ?: "",
                                classroomName = movementClassroomName,
                            )
                            students.add(studentStatus)
                        }
                    }
            }
        }

        return QueryStudentStatusList(students)
    }

    private fun movementStudent(status: Status?): String {
        var moveClassroomName = ""
        if (status?.type == StatusType.MOVEMENT) {
            val classroomMovement = queryClassroomMovementSpi.queryClassroomMovementByStatus(status)
            val moveClassroom = queryClassroomSpi.queryClassroomById(classroomMovement.classroomId)
            moveClassroomName = moveClassroom.name
        }
        return moveClassroomName
    }

    private fun checkUserNumLessThanTen(userNum: Int) =
        if (userNum < 10) {
            "0$userNum"
        } else {
            userNum.toString()
        }
}
