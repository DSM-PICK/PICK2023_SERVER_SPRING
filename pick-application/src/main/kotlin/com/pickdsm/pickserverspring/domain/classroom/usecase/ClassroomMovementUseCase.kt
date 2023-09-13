package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolNotFoundException
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
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryMovementStudentList
import com.pickdsm.pickserverspring.domain.classroom.exception.AfterSchoolCannotMovementException
import com.pickdsm.pickserverspring.domain.classroom.exception.CannotMovementException
import com.pickdsm.pickserverspring.domain.classroom.exception.CannotMovementMyClassroom
import com.pickdsm.pickserverspring.domain.classroom.exception.CannotMovementWeekendException
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomMovementStudentNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.CommandClassroomMovementSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomMovementSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.exception.ClubNotFoundException
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.TypeNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QueryTypeSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.TimeQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.time.exception.TimeNotFoundException
import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.dto.request.UserInfoRequest
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

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
    private val queryClubSpi: QueryClubSpi,
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
    private val queryTypeSpi: QueryTypeSpi,
) : ClassroomMovementApi {

    override fun saveClassroomMovement(request: DomainClassroomMovementRequest) {
        val classroom = queryClassroomSpi.queryClassroomById(request.classroomId)
            ?: throw ClassroomNotFoundException
        val student = userSpi.queryUserInfoByUserId(userSpi.getCurrentUserId())

        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())
        val time = timeList.timeList.find { time -> time.period == request.period }
            ?: throw TimeNotFoundException

        val statusTypes = queryStatusSpi.queryStatusTypesByStudentIdAndEndPeriodAndToday(
            studentId = student.id,
            period = request.period,
        )

        val todayType = queryTypeSpi.queryDirectorTypeByDate(LocalDate.now())
            ?: throw TypeNotFoundException

        when (todayType) {
            DirectorType.AFTER_SCHOOL -> {
                throw AfterSchoolCannotMovementException
            }

            DirectorType.TUE_CLUB, DirectorType.FRI_CLUB -> {
                val userClubClassroomId = queryClubSpi.queryClubClassroomIdByStudentId(student.id)
                    ?: throw ClubNotFoundException

                checkIsMovementMyClassroom(
                    requestMovementClassroomId = classroom.id,
                    existingClassroomId = userClubClassroomId,
                )
            }

            DirectorType.SELF_STUDY -> {
                val userClassroomId = queryClassroomSpi.queryClassroomIdByGradeAndClassNum(
                    grade = student.grade,
                    classNum = student.classNum,
                )

                checkIsMovementMyClassroom(
                    requestMovementClassroomId = classroom.id,
                    existingClassroomId = userClassroomId,
                )
            }
        }

        checkIsStatusPicnicOrAwait(statusTypes)
        checkIsWeekends()

        when (queryClassroomMovementSpi.existClassroomMovementByStudentId(student.id)) {
            true -> {
                val existClassroomMovementStatus = queryClassroomMovementSpi.queryClassroomMovementByStudentIdAndToday(student.id)
                    ?: throw ClassroomMovementStudentNotFoundException

                commandClassroomMovementSpi.saveClassroomMovement(
                    existClassroomMovementStatus.changeClassroomId(request.classroomId),
                )
            }

            false -> {
                val newClassroomMovementStatus = Status(
                    studentId = student.id,
                    teacherId = UUID(0, 0), // TODO: 해당 층 자습감독쌤 아이디 넣기
                    startPeriod = time.period,
                    endPeriod = 10,
                    type = StatusType.MOVEMENT,
                )
                val saveStatusId = statusCommandTeacherSpi.saveStatusAndGetStatusId(newClassroomMovementStatus)
                commandClassroomMovementSpi.saveClassroomMovement(
                    ClassroomMovement(
                        classroomId = classroom.id,
                        statusId = saveStatusId,
                    ),
                )
            }
        }
    }

    private fun checkIsWeekends() {
        val isWeekend = LocalDate.now().dayOfWeek > DayOfWeek.FRIDAY
        if (isWeekend) {
            throw CannotMovementWeekendException
        }
    }

    private fun checkIsMovementMyClassroom(
        requestMovementClassroomId: UUID,
        existingClassroomId: UUID?,
    ) {
        val isEqRequestMovementClassroomAndExistClassroom = requestMovementClassroomId == existingClassroomId

        if (isEqRequestMovementClassroomAndExistClassroom) {
            throw CannotMovementMyClassroom
        }
    }

    private fun checkIsStatusPicnicOrAwait(statusTypes: List<StatusType>) {
        val isExistStatusPicnicOrAwait = statusTypes.contains(StatusType.PICNIC) || statusTypes.contains(StatusType.AWAIT)
        if (isExistStatusPicnicOrAwait) {
            throw CannotMovementException
        }
    }

    override fun queryMovementStudentList(
        grade: Int?,
        classNum: Int?,
        floor: Int?,
    ): QueryMovementStudentList {
        val todayMovementStudentInfoList = queryStatusSpi.queryMovementStudentInfoListByToday(LocalDate.now())
        val todayMovementStudentIdList = todayMovementStudentInfoList.map { movement -> movement.studentId }
        val userIdRequest = UserInfoRequest(todayMovementStudentIdList)
        val userList = userQueryApplicationSpi.queryUserInfo(userIdRequest)
        val movementStudent = mutableListOf<MovementStudentElement>()
        val todayType = queryTypeSpi.queryDirectorTypeByDate(LocalDate.now())
            ?: throw TypeNotFoundException

        if (floor == null) {
            val moveList = userList.filter { user ->
                user.grade == grade && user.classNum == classNum
            }.map { user ->
                val status = queryStatusSpi.queryMovementStudentByStudentId(user.id)
                    ?: throw StatusNotFoundException
                val classroomMovement = queryClassroomMovementSpi.queryClassroomMovementByStatus(status)
                    ?: throw ClassroomMovementStudentNotFoundException
                val classroomName = queryClassroomSpi.queryClassroomNameByClassroomId(classroomMovement.classroomId)
                    ?: throw ClassroomNotFoundException
                val number = user.grade.toString() + "-" + user.classNum
                val gradeForMovement = getGrade(todayType, number, user.id)

                user.toMovementStudentElement(gradeForMovement, classroomName)
            }
            movementStudent.addAll(moveList)
        } else {
            val moveList = userList
                .map { user ->
                    val status = queryStatusSpi.queryMovementStudentByStudentId(user.id)
                        ?: throw StatusNotFoundException
                    val classroomMovement = queryClassroomMovementSpi.queryClassroomMovementByStatus(status)
                        ?: throw ClassroomMovementStudentNotFoundException
                    val classroom = queryClassroomSpi.queryClassroomById(classroomMovement.classroomId)
                        ?: throw ClassroomNotFoundException
                    val number = user.grade.toString() + "-" + user.classNum
                    val gradeForMovement = getGrade(todayType, number, user.id)

                    if (classroom.floor == floor) {
                        user.toMovementStudentElement(gradeForMovement, classroom.name)
                    } else {
                        throw ClassroomMovementStudentNotFoundException
                    }
                }
            movementStudent.addAll(moveList)
        }
        return QueryMovementStudentList(movementStudent.sortedBy { it.studentNumber })
    }

    private fun User.toMovementStudentElement(gradeForMovement: String, classroomName: String) =
        MovementStudentElement(
            studentNumber = "${this.grade}${this.classNum}${this.paddedUserNum()}",
            studentName = this.name,
            before = gradeForMovement,
            after = classroomName,
        )

    private fun getGrade(directorType: DirectorType, classroom: String, studentId: UUID): String {
        when (directorType) {
            DirectorType.SELF_STUDY -> {
                return classroom
            }

            DirectorType.TUE_CLUB, DirectorType.FRI_CLUB -> {
                val clubClassroomId = queryClubSpi.queryClubClassroomIdByStudentId(studentId)
                    ?: throw ClubNotFoundException
                return queryClassroomSpi.queryClassroomNameByClassroomId(clubClassroomId)
                    ?: throw ClassroomNotFoundException
            }

            DirectorType.AFTER_SCHOOL -> {
                val afterSchoolClassroomId = queryAfterSchoolSpi.queryAfterSchoolClassroomIdByStudentId(studentId)
                    ?: throw AfterSchoolNotFoundException
                return queryClassroomSpi.queryClassroomNameByClassroomId(afterSchoolClassroomId)
                    ?: throw ClassroomNotFoundException
            }

            else -> return ""
        }
    }

    private fun User.paddedUserNum(): String = this.num.toString().padStart(2, '0')

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
        val userInfo = userSpi.queryUserInfoByUserId(userSpi.getCurrentUserId())
        val statusId = queryStatusSpi.queryMovementStudentStatusIdByStudentIdAndToday(userInfo.id)
            ?: throw StatusNotFoundException
        val movementClassroomId = queryClassroomMovementSpi.queryClassroomMovementClassroomIdByStatusId(statusId)
            ?: throw ClassroomMovementStudentNotFoundException
        val classroomName = queryClassroomSpi.queryClassroomNameByClassroomId(movementClassroomId)
            ?: throw ClassroomNotFoundException

        return QueryClassroomMovementLocationResponse(
            name = userInfo.name,
            locationClassroom = classroomName,
        )
    }
}
