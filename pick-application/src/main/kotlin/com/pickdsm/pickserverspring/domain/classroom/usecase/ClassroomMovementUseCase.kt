package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.admin.api.AdminApi
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.spi.CommandStatusSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.application.spi.UserQueryApplicationSpi
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomMovementApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.request.DomainClassroomMovementRequest
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.MovementStudentElement
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomMovementLocationResponse
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomMovementStudentList
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryMovementStudentList
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomMovementStudentNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.CommandClassroomMovementSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomMovementSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.TimeQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.time.exception.TimeNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate
import java.util.*

@UseCase
class ClassroomMovementUseCase(
    private val queryClassroomSpi: QueryClassroomSpi,
    private val userSpi: UserSpi,
    private val commandClassroomMovementSpi: CommandClassroomMovementSpi,
    private val commandStatusSpi: CommandStatusSpi,
    private val timeQueryTeacherSpi: TimeQueryTeacherSpi,
    private val statusCommandTeacherSpi: StatusCommandTeacherSpi,
    private val queryStatusSpi: QueryStatusSpi,
    private val userQueryApplicationSpi: UserQueryApplicationSpi,
    private val queryClassroomMovementSpi: QueryClassroomMovementSpi,
    private val adminApi: AdminApi,
    private val queryClubSpi: QueryClubSpi,
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
) : ClassroomMovementApi {

    override fun saveClassroomMovement(request: DomainClassroomMovementRequest) {
        val classroom = queryClassroomSpi.queryClassroomById(request.classroomId)
            ?: throw ClassroomNotFoundException
        val studentId = userSpi.getCurrentUserId()
        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())
        val time = timeList.timeList.find { time -> time.period == request.period }
            ?: throw TimeNotFoundException

        val status = Status(
            studentId = studentId,
            teacherId = UUID(0, 0), // TODO: 해당 층 자습감독쌤 아이디 넣기
            startPeriod = time.period,
            endPeriod = 10,
            type = StatusType.MOVEMENT,
        )
        val saveStatusId = statusCommandTeacherSpi.saveStatusAndGetStatusId(status)
        commandClassroomMovementSpi.saveClassroomMovement(
            ClassroomMovement(
                classroomId = classroom.id,
                statusId = saveStatusId,
            ),
        )
    }

    override fun queryMovementStudentList(
        grade: Int?,
        classNum: Int?,
        floor: Int?,
    ): QueryMovementStudentList {
        val todayMovementStudentInfoList = queryStatusSpi.queryMovementStudentInfoListByToday(LocalDate.now())
        val todayMovementStudentIdList = todayMovementStudentInfoList.map { movement -> movement.studentId }
        val userList = userQueryApplicationSpi.queryUserInfo(todayMovementStudentIdList)
        val movementStudent = mutableListOf<MovementStudentElement>()
        val studentAttendanceList = adminApi.getTypeByDate(LocalDate.now())

        if (floor == 0) {
            val moveList = userList.filter {
                it.grade == grade && it.classNum == classNum
            }.map {
                val status = queryStatusSpi.queryMovementStudentByStudentId(it.id)
                val classroomMovement = queryClassroomMovementSpi.queryClassroomMovementByStatus(status!!)
                    ?: throw ClassroomMovementStudentNotFoundException
                val classroom = queryClassroomSpi.queryClassroomById(classroomMovement.classroomId)
                    ?: throw ClassroomNotFoundException
                val number = it.grade.toString() + "-" + it.classNum
                val gradeForMovement = getGrade(studentAttendanceList.type, number, it.id)

                MovementStudentElement(
                    studentNumber = "${it.grade}${it.classNum}${checkUserNumLessThanTen(it.num)}",
                    studentName = it.name,
                    before = gradeForMovement,
                    after = classroom.name,
                )
            }
            movementStudent.addAll(moveList)
        } else {
            val moveList = userList
                .map {
                    val status = queryStatusSpi.queryMovementStudentByStudentId(it.id)
                        ?: throw StatusNotFoundException
                    val classroomMovement = queryClassroomMovementSpi.queryClassroomMovementByStatus(status)
                        ?: throw ClassroomMovementStudentNotFoundException
                    val classroom = queryClassroomSpi.queryClassroomById(classroomMovement.classroomId)
                        ?: throw ClassroomNotFoundException
                    val number = it.grade.toString() + "-" + it.classNum
                    val gradeForMovement = getGrade(studentAttendanceList.type, number, it.id)

                    if (classroom.floor == floor) {
                        MovementStudentElement(
                            studentNumber = "${it.grade}${it.classNum}${checkUserNumLessThanTen(it.num)}",
                            studentName = it.name,
                            before = gradeForMovement,
                            after = classroom.name,
                        )
                    } else {
                        throw ClassroomMovementStudentNotFoundException
                    }
                }
            movementStudent.addAll(moveList)
        }
        return QueryMovementStudentList(movementStudent)
    }

    override fun returnClassroomMovement() {
        val currentStudentId = userSpi.getCurrentUserId()
        val status = queryStatusSpi.queryMovementStudentByStudentId(currentStudentId)
        val classroom = status?.let { queryClassroomMovementSpi.queryClassroomMovementByStatus(it) }

        if (classroom != null) {
            commandClassroomMovementSpi.deleteClassroomMovement(classroom)
        }
        if (status != null) {
            commandStatusSpi.deleteStatus(status)
        }
    }

    override fun getClassroomMovementLocation(): QueryClassroomMovementLocationResponse {
        val userId = userSpi.getCurrentUserId()
        val userInfo = userSpi.queryUserInfoByUserId(userId)
        val status = queryStatusSpi.queryMovementStudentByStudentId(userId)
            ?: throw StatusNotFoundException
        val classroomMovement = queryClassroomMovementSpi.queryClassroomMovementByStatus(status)
            ?: throw ClassroomMovementStudentNotFoundException
        val classroom = queryClassroomSpi.queryClassroomById(classroomMovement.classroomId)
            ?: throw ClassroomNotFoundException

        return QueryClassroomMovementLocationResponse(
            name = userInfo.name,
            locationClassroom = classroom.name,
        )
    }

    private fun checkUserNumLessThanTen(userNum: Int) =
        if (userNum < 10) {
            "0$userNum"
        } else {
            userNum.toString()
        }

    private fun getGrade(directorType: DirectorType, classroom: String, studentId: UUID): String {
        when (directorType) {
            DirectorType.SELF_STUDY -> {
                return classroom
            }

            DirectorType.CLUB -> {
                val classroomId = queryClubSpi.queryClubIdByStudentId(studentId)
                val classroomForClub = queryClassroomSpi.queryClassroomById(classroomId)
                    ?: throw ClassroomNotFoundException
                return classroomForClub.name
            }

            DirectorType.AFTER_SCHOOL -> {
                val classroomId = queryAfterSchoolSpi.queryAfterSchoolIdByStudentId(studentId)
                val classroomForAfterSchool = queryClassroomSpi.queryClassroomById(classroomId)
                    ?: throw ClassroomNotFoundException
                return classroomForAfterSchool.name
            }

            else -> return ""
        }
    }
}
