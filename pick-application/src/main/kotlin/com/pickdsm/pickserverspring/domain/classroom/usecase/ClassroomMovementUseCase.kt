package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.admin.api.AdminApi
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolNotFoundException
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.spi.CommandStatusSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.application.spi.UserQueryApplicationSpi
import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomMovementApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.request.DomainClassroomMovementRequest
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.MovementStudentElement
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomMovementLocationResponse
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryMovementStudentList
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
    private val adminApi: AdminApi,
    private val queryClubSpi: QueryClubSpi,
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
    private val queryTypeSpi: QueryTypeSpi,
) : ClassroomMovementApi {

    override fun saveClassroomMovement(request: DomainClassroomMovementRequest) {
        val classroom = getClassroomByClassroomId(request.classroomId)

        val student = userSpi.queryUserInfoByUserId(userSpi.getCurrentUserId())
        val studentId = student.id

        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())
        val time = timeList.timeList.find { time -> time.period == request.period }
            ?: throw TimeNotFoundException

        val statusTypes = queryStatusSpi.queryStatusTypesByStudentIdAndEndPeriod(
            studentId = studentId,
            period = request.period,
        )

        val todayType = queryTypeSpi.queryDirectorTypeByDate(LocalDate.now())
            ?: throw TypeNotFoundException

        when (todayType) {
            DirectorType.AFTER_SCHOOL -> {
                val userAfterSchoolClassroomId = queryAfterSchoolSpi.queryAfterSchoolClassroomIdByStudentId(studentId)
                    ?: throw AfterSchoolNotFoundException
                val userAfterSchoolClassroom = getClassroomByClassroomId(userAfterSchoolClassroomId)

                checkIsMovementMyClassroom(
                    requestMovementClassroom = classroom,
                    existingClassroom = userAfterSchoolClassroom,
                )
            }

            DirectorType.TUE_CLUB, DirectorType.FRI_CLUB -> {
                val userClubClassroomId = queryClubSpi.queryClubClassroomIdByStudentId(studentId)
                    ?: throw ClubNotFoundException
                val userClubClassroom = getClassroomByClassroomId(userClubClassroomId)

                checkIsMovementMyClassroom(
                    requestMovementClassroom = classroom,
                    existingClassroom = userClubClassroom,
                )
            }

            DirectorType.SELF_STUDY -> {
                val isEqGradeAndClassNum = student.grade == classroom.grade && student.classNum == classroom.classNum
                if (isEqGradeAndClassNum) {
                    throw CannotMovementMyClassroom
                }
            }
        }

        checkIsStatusPicnic(statusTypes)
        checkIsWeekends()

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

    private fun getClassroomByClassroomId(classroomId: UUID): Classroom =
        queryClassroomSpi.queryClassroomById(classroomId)
            ?: throw ClassroomNotFoundException

    private fun checkIsWeekends() {
        val isWeekend = LocalDate.now().dayOfWeek > DayOfWeek.FRIDAY
        if (isWeekend) {
            throw CannotMovementWeekendException
        }
    }

    private fun checkIsMovementMyClassroom(
        requestMovementClassroom: Classroom,
        existingClassroom: Classroom,
    ) {
        val isEqRequestMovementClassroomAndExistClassroom =
            requestMovementClassroom.grade == existingClassroom.grade && requestMovementClassroom.classNum == existingClassroom.classNum
        if (isEqRequestMovementClassroomAndExistClassroom) {
            throw CannotMovementMyClassroom
        }
    }

    private fun checkIsStatusPicnic(statusTypes: List<StatusType>) {
        val isExistStatusPicnic = statusTypes.contains(StatusType.PICNIC)
        if (isExistStatusPicnic) {
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
        val studentAttendanceList = adminApi.getTypeByDate(LocalDate.now())

        if (floor == null) {
            val moveList = userList.filter {
                it.grade == grade && it.classNum == classNum
            }.map {
                val status = queryStatusSpi.queryMovementStudentByStudentId(it.id)
                    ?: throw StatusNotFoundException
                val classroomMovement = queryClassroomMovementSpi.queryClassroomMovementByStatus(status)
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
        return QueryMovementStudentList(movementStudent.sortedBy { it.studentNumber })
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

            DirectorType.TUE_CLUB, DirectorType.FRI_CLUB -> {
                val clubClassroomId = queryClubSpi.queryClubIdByStudentId(studentId)
                    ?: throw ClubNotFoundException
                val classroomForClub = queryClassroomSpi.queryClassroomById(clubClassroomId)
                    ?: throw ClassroomNotFoundException
                return classroomForClub.name
            }

            DirectorType.AFTER_SCHOOL -> {
                val afterSchoolClassroomId = queryAfterSchoolSpi.queryAfterSchoolIdByStudentId(studentId)
                    ?: throw AfterSchoolNotFoundException
                val classroomForAfterSchool = queryClassroomSpi.queryClassroomById(afterSchoolClassroomId)
                    ?: throw ClassroomNotFoundException
                return classroomForAfterSchool.name
            }

            else -> return ""
        }
    }
}
