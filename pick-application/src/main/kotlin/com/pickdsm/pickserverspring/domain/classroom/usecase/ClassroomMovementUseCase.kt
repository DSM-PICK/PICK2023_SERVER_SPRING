package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.admin.api.AdminApi
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolStudentNotFoundException
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.application.spi.UserQueryApplicationSpi
import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomMovementApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.request.DomainClassroomMovementRequest
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.MovementStudentElement
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryMovementStudentList
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
        val studentId = userSpi.getCurrentUserId()
        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())
        val time = timeList.timeList.find { time -> time.period == request.period }
            ?: throw TimeNotFoundException

        val status = Status(
            studentId = studentId,
            teacherId = studentId, // TODO: 해당 층 자습감독쌤 아이디 넣기
            startPeriod = time.period,
            endPeriod = time.period,
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
        date: LocalDate,
    ): QueryMovementStudentList {
        val todayMovementStudentInfoList = queryStatusSpi.queryMovementStudentInfoListByToday(LocalDate.now())
        val todayMovementStudentIdList = todayMovementStudentInfoList.map { movement -> movement.studentId }
        val userList = userQueryApplicationSpi.queryUserInfo(todayMovementStudentIdList)
        val movementStudent = mutableListOf<MovementStudentElement>()
        val studentAttendanceList = adminApi.getTypeByDate(LocalDate.now())

        if (floor == null) {
            val moveList = userList.filter {
                it.grade == grade && it.classNum == classNum
            }.map {
                val status = queryStatusSpi.queryMovementStudentByStudentId(it.id)
                val classroomMovement = queryClassroomMovementSpi.queryClassroomMovementByStatus(status!!)
                val classroom = queryClassroomSpi.queryClassroomById(classroomMovement.classroomId)
                val gradeForMovement = getGrade(studentAttendanceList.type, classroom, it.id)

                MovementStudentElement(
                    studentNumber = "${grade}${classNum}${checkUserNumLessThanTen(it.num)}",
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
                    val classroomMovement = queryClassroomMovementSpi.queryClassroomMovementByStatus(status!!)
                    val classroom = queryClassroomSpi.queryClassroomById(classroomMovement.classroomId)
                    val gradeForMovement = getGrade(studentAttendanceList.type, classroom, it.id)

                    if (classroom.floor == floor) {
                        MovementStudentElement(
                            studentNumber = "${grade}${classNum}${checkUserNumLessThanTen(it.num)}",
                            studentName = it.name,
                            before = gradeForMovement,
                            after = classroom.name,
                        )
                    } else {
                        throw AfterSchoolStudentNotFoundException
                    }
                }
            movementStudent.addAll(moveList)
        }
        return QueryMovementStudentList(movementStudent)
    }

    private fun checkUserNumLessThanTen(userNum: Int) =
        if (userNum < 10) {
            "0$userNum"
        } else {
            userNum.toString()
        }

    private fun getGrade(directorType: DirectorType, classroom: Classroom, studentId: UUID): String {
        when (directorType) {
            DirectorType.SELF_STUDY -> {
                return classroom.grade.toString() + "-" + classroom.classNum.toString()
            }
            DirectorType.CLUB -> {
                val classroomId = queryClubSpi.queryClubIdByStudentId(studentId)
                val classroomForClub = queryClassroomSpi.queryClassroomById(classroomId)
                return classroomForClub.name
            }
            DirectorType.AFTER_SCHOOL -> {
                val classroomId = queryAfterSchoolSpi.queryAfterSchoolIdByStudentId(studentId)
                val classroomForAfterSchool = queryClassroomSpi.queryClassroomById(classroomId)
                return classroomForAfterSchool.name
            }
            else -> return ""
        }
    }
}
