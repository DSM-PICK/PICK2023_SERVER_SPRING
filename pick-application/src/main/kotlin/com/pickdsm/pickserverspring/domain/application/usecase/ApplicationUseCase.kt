package com.pickdsm.pickserverspring.domain.application.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.api.ApplicationApi
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainPicnicAcceptOrRefuseRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainPicnicPassRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryMyPicnicEndTimeResponse
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryMyPicnicInfoResponse
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationElement
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicStudentElement
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicStudentList
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryStudentStatusElement
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryStudentStatusList
import com.pickdsm.pickserverspring.domain.application.exception.AlreadyApplicationPicnicOrAlreadyPicnicException
import com.pickdsm.pickserverspring.domain.application.exception.ApplicationNotFoundException
import com.pickdsm.pickserverspring.domain.application.exception.CannotApplicationWeekendException
import com.pickdsm.pickserverspring.domain.application.exception.OverEndTimeException
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.spi.CommandApplicationSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryApplicationSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.application.spi.UserQueryApplicationSpi
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomMovementSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QueryTypeSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.TimeQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.time.Time
import com.pickdsm.pickserverspring.domain.time.exception.TimeNotFoundException
import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.dto.request.UserInfoRequest
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
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
    private val queryClubSpi: QueryClubSpi,
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
    private val queryTypeSpi: QueryTypeSpi,
) : ApplicationApi {

    override fun saveApplicationToGoOut(request: DomainApplicationGoOutRequest) {
        val studentId = userSpi.getCurrentUserId()
        checkIsExistAwaitOrPicnicStatus(studentId)
        checkIsWeekends()

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
                isReturn = false,
            ),
        )
    }

    private fun checkIsExistAwaitOrPicnicStatus(studentId: UUID) {
        if (queryStatusSpi.existAwaitOrPicnicStatusByStudentId(studentId)) {
            throw AlreadyApplicationPicnicOrAlreadyPicnicException
        }
    }

    private fun checkIsWeekends() {
        if (LocalDate.now().dayOfWeek > DayOfWeek.FRIDAY) {
            throw CannotApplicationWeekendException
        }
    }

    override fun queryPicnicApplicationListByGradeAndClassNum(
        grade: String?,
        classNum: String?,
        floor: Int?,
        type: DirectorType,
    ): QueryPicnicApplicationList {
        val todayOutingList = queryApplicationSpi.queryPicnicApplicationListByToday()
        val todayStatusList = queryStatusSpi.queryAwaitStudentListByToday()
        val todayApplicationStudentIdList = todayStatusList.map { it.studentId }
        val userIdRequest = UserInfoRequest(todayApplicationStudentIdList)
        val userList = userQueryApplicationSpi.queryUserInfo(userIdRequest)

        val outing = mutableListOf<QueryPicnicApplicationElement>()

        val studentGrade = grade?.toIntOrNull() ?: 0
        val studentClassNum = classNum?.toIntOrNull() ?: 0

        when (type) {
            DirectorType.SELF_STUDY -> {
                when (floor) {
                    null -> {
                        todayStatusList.filteringByGradeAndClassNum(
                            userList = userList,
                            studentGrade = studentGrade,
                            studentClassNum = studentClassNum,
                        ).map { status ->
                            val user = userList.findUserByStudentId(status.studentId)
                                ?: throw UserNotFoundException
                            val startTime = getTodayTimeByPeriod(status.startPeriod)?.startTime
                                ?: throw TimeNotFoundException
                            val endTime = getTodayTimeByPeriod(status.endPeriod)?.endTime
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplications = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${user.paddedUserNum()}",
                                studentName = user.name,
                                startTime = startTime,
                                endTime = endTime,
                                reason = application.reason,
                            )

                            outing.add(picnicApplications)
                        }
                    }

                    else -> {
                        todayStatusList.filter { status ->
                            val user = userList.findUserByStudentId(status.studentId) ?: return@filter false
                            val classroomGrade = queryClassroomSpi.queryClassroomGradeByFloor(floor)
                            classroomGrade == user.grade
                        }.map { status ->
                            val user = userList.findUserByStudentId(status.studentId)
                                ?: throw UserNotFoundException
                            val startTime = getTodayTimeByPeriod(status.startPeriod)?.startTime
                                ?: throw TimeNotFoundException
                            val endTime = getTodayTimeByPeriod(status.endPeriod)?.endTime
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplication = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${user.paddedUserNum()}",
                                studentName = user.name,
                                startTime = startTime,
                                endTime = endTime,
                                reason = application.reason,
                            )
                            outing.add(picnicApplication)
                        }
                    }
                }
            }

            DirectorType.TUE_CLUB, DirectorType.FRI_CLUB -> {
                when (floor) {
                    null -> {
                        todayStatusList.filteringByGradeAndClassNum(
                            userList = userList,
                            studentGrade = studentGrade,
                            studentClassNum = studentClassNum,
                        ).map { status ->
                            val user = userList.findUserByStudentId(status.studentId)
                                ?: throw UserNotFoundException
                            val startTime = getTodayTimeByPeriod(status.startPeriod)?.startTime
                                ?: throw TimeNotFoundException
                            val endTime = getTodayTimeByPeriod(status.endPeriod)?.endTime
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplications = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${user.paddedUserNum()}",
                                studentName = user.name,
                                startTime = startTime,
                                endTime = endTime,
                                reason = application.reason,
                            )

                            outing.add(picnicApplications)
                        }
                    }

                    else -> {
                        todayStatusList.filter { status ->
                            val user = userList.findUserByStudentId(status.studentId) ?: return@filter false
                            val clubStudentId = queryClubSpi.queryClubStudentIdListByFloor(floor).find { user.id == it }
                            clubStudentId == user.id
                        }.map { status ->
                            val user = userList.findUserByStudentId(status.studentId)
                                ?: throw UserNotFoundException
                            val startTime = getTodayTimeByPeriod(status.startPeriod)?.startTime
                                ?: throw TimeNotFoundException
                            val endTime = getTodayTimeByPeriod(status.endPeriod)?.endTime
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplications = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${user.paddedUserNum()}",
                                studentName = user.name,
                                startTime = startTime,
                                endTime = endTime,
                                reason = application.reason,
                            )

                            outing.add(picnicApplications)
                        }
                    }
                }
            }

            DirectorType.AFTER_SCHOOL -> {
                when (floor) {
                    null -> {
                        todayStatusList.filteringByGradeAndClassNum(
                            userList = userList,
                            studentGrade = studentGrade,
                            studentClassNum = studentClassNum,
                        ).map { status ->
                            val user = userList.findUserByStudentId(status.studentId)
                                ?: throw UserNotFoundException
                            val startTime = getTodayTimeByPeriod(status.startPeriod)?.startTime
                                ?: throw TimeNotFoundException
                            val endTime = getTodayTimeByPeriod(status.endPeriod)?.endTime
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplications = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${user.paddedUserNum()}",
                                studentName = user.name,
                                startTime = startTime,
                                endTime = endTime,
                                reason = application.reason,
                            )

                            outing.add(picnicApplications)
                        }
                    }

                    else -> {
                        todayStatusList.filter { status ->
                            val user = userList.findUserByStudentId(status.studentId) ?: return@filter false
                            val afterSchoolStudentId = queryAfterSchoolSpi.queryAfterSchoolStudentIdByFloor(floor).find { user.id == it }
                            afterSchoolStudentId == user.id
                        }.map { status ->
                            val user = userList.findUserByStudentId(status.studentId)
                                ?: throw UserNotFoundException
                            val startTime = getTodayTimeByPeriod(status.startPeriod)?.startTime
                                ?: throw TimeNotFoundException
                            val endTime = getTodayTimeByPeriod(status.endPeriod)?.endTime
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplications = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${user.paddedUserNum()}",
                                studentName = user.name,
                                startTime = startTime,
                                endTime = endTime,
                                reason = application.reason,
                            )

                            outing.add(picnicApplications)
                        }
                    }
                }
            }
        }

        outing.sortedWith(
            compareBy(QueryPicnicApplicationElement::endTime)
                .thenBy(QueryPicnicApplicationElement::startTime)
                .thenBy(QueryPicnicApplicationElement::studentNumber),
        )

        return QueryPicnicApplicationList(outing)
    }

    private fun List<Status>.filteringByGradeAndClassNum(
        userList: List<User>,
        studentGrade: Int,
        studentClassNum: Int,
    ): List<Status> =
        this.filter { status ->
            val user = userList.findUserByStudentId(status.studentId) ?: return@filter false

            when {
                studentGrade != 0 && studentClassNum == 0 -> studentGrade == user.grade
                studentGrade == 0 -> false
                else -> (studentGrade == user.grade && studentClassNum == user.classNum)
            }
        }

    override fun queryPicnicStudentListByToday(): QueryPicnicStudentList {
        val todayPicnicStudentInfoList = queryStatusSpi.queryPicnicStudentInfoListByToday(LocalDate.now())
        val todayPicnicStudentIdList = todayPicnicStudentInfoList.map { status -> status.studentId }
        val userIdRequest = UserInfoRequest(todayPicnicStudentIdList)
        val userList = userQueryApplicationSpi.queryUserInfo(userIdRequest)

        val outing: List<QueryPicnicStudentElement> = todayPicnicStudentInfoList
            .map { status ->
                val user = userList.findUserByStudentId(status.studentId)
                    ?: throw UserNotFoundException
                val endTime = getTodayTimeByPeriod(status.endPeriod)?.endTime
                    ?: throw TimeNotFoundException

                QueryPicnicStudentElement(
                    studentId = user.id,
                    studentNumber = "${user.grade}${user.classNum}${user.paddedUserNum()}",
                    studentName = user.name,
                    endTime = endTime,
                )
            }
            .sortedWith(
                compareBy(QueryPicnicStudentElement::endTime)
                    .thenBy(QueryPicnicStudentElement::studentNumber),
            )

        return QueryPicnicStudentList(outing)
    }

    override fun queryAllStudentStatusByClassroomId(classroomId: UUID): QueryStudentStatusList {
        val dateType = queryTypeSpi.queryDirectorTypeByDate(LocalDate.now()) ?: DirectorType.SELF_STUDY
        val todayStudentStatusList = queryStatusSpi.queryStatusListByToday()
        val classroom = queryClassroomSpi.queryClassroomById(classroomId) ?: throw ClassroomNotFoundException
        val students = mutableListOf<QueryStudentStatusElement>()

        when (dateType) {
            DirectorType.AFTER_SCHOOL -> {
                val afterSchoolList = queryAfterSchoolSpi.queryAfterSchoolListByClassroomId(classroomId)
                val afterSchoolStudentIdList = afterSchoolList.map { it.studentId }
                val userIdRequest = UserInfoRequest(afterSchoolStudentIdList)
                val afterSchoolUserInfos = userSpi.queryUserInfo(userIdRequest)

                afterSchoolUserInfos.map { user ->
                    val status = todayStudentStatusList.find { user.id == it.studentId }
                    val movementClassroomName = movementStudent(status)

                    val studentStatus = QueryStudentStatusElement(
                        studentId = user.id,
                        studentNumber = "${user.grade}${user.classNum}${user.paddedUserNum()}",
                        studentName = user.name,
                        type = status?.type?.name ?: StatusType.ATTENDANCE.name,
                        classroomName = movementClassroomName,
                    )
                    students.add(studentStatus)
                }
            }

            DirectorType.TUE_CLUB, DirectorType.FRI_CLUB -> {
                val clubList = queryClubSpi.queryClubListByClassroomId(classroomId)
                val clubStudentIdList = clubList.map { it.studentId }
                val userIdRequest = UserInfoRequest(clubStudentIdList)
                val clubUserInfos = userSpi.queryUserInfo(userIdRequest)

                clubUserInfos.map { user ->
                    val status = todayStudentStatusList.find { user.id == it.studentId }
                    val movementClassroomName = movementStudent(status)

                    val studentStatus = QueryStudentStatusElement(
                        studentId = user.id,
                        studentNumber = "${user.grade}${user.classNum}${user.paddedUserNum()}",
                        studentName = user.name,
                        type = status?.type?.name ?: StatusType.ATTENDANCE.name,
                        classroomName = movementClassroomName,
                    )
                    students.add(studentStatus)
                }
            }

            DirectorType.SELF_STUDY -> {
                val classroomUserInfos = userSpi.queryUserInfoByGradeAndClassNum(
                    grade = classroom.grade,
                    classNum = classroom.classNum,
                )

                classroomUserInfos.map { user ->
                    val status = todayStudentStatusList.find { user.id == it.studentId }
                    val movementClassroomName = movementStudent(status)

                    val studentStatus = QueryStudentStatusElement(
                        studentId = user.id,
                        studentNumber = user.num,
                        studentName = user.name,
                        type = status?.type?.name ?: StatusType.ATTENDANCE.name,
                        classroomName = movementClassroomName,
                    )
                    students.add(studentStatus)
                }
            }
        }
        return QueryStudentStatusList(students.sortedBy { it.studentNumber })
    }

    override fun savePicnicPass(request: DomainPicnicPassRequest) {
        val teacherId = userSpi.getCurrentUserId()
        val userIdList = request.userIdList

        isExistPicnicOrAwaitOrMovementStudent(userIdList)

        val userIdRequest = UserInfoRequest(userIdList)
        val userList = userSpi.queryUserInfo(userIdRequest)

        userIdList.map {
            val user = userList.findUserByStudentId(it)
                ?: throw UserNotFoundException

            val saveStatusId = statusCommandTeacherSpi.saveStatusAndGetStatusId(
                Status(
                    studentId = user.id,
                    teacherId = teacherId,
                    startPeriod = request.startPeriod,
                    endPeriod = request.endPeriod,
                    type = StatusType.PICNIC,
                ),
            )

            commandApplicationSpi.saveApplication(
                Application(
                    reason = request.reason,
                    statusId = saveStatusId,
                    isReturn = false,
                ),
            )
        }
    }

    private fun isExistPicnicOrAwaitOrMovementStudent(userIdList: List<UUID>) {
        val picnicOrAwaitOrMovementStudentIds = queryStatusSpi.queryPicnicOrAwaitOrMovementStatusStudentIdListByToday()
        val isExistPicnicOrAwaitOrMovement = picnicOrAwaitOrMovementStudentIds.containsAll(userIdList)

        if (isExistPicnicOrAwaitOrMovement) {
            throw AlreadyApplicationPicnicOrAlreadyPicnicException
        }
    }

    override fun savePicnicAcceptOrRefuse(request: DomainPicnicAcceptOrRefuseRequest) {
        val teacherId = userSpi.getCurrentUserId()
        val userIdRequest = UserInfoRequest(request.userIdList)
        val userList = userSpi.queryUserInfo(userIdRequest)
        val todayAwaitStatusList = queryStatusSpi.queryAwaitStudentListByToday()

        when (request.type) {
            StatusType.PICNIC -> {
                val statusList = request.userIdList.map { userId ->
                    val user = userList.findUserByStudentId(userId)
                        ?: throw UserNotFoundException
                    val status = todayAwaitStatusList.find { user.id == it.studentId }
                        ?: throw StatusNotFoundException

                    status.changePicnicStatus(
                        studentId = user.id,
                        teacherId = teacherId,
                        type = StatusType.PICNIC,
                    )
                }

                statusCommandTeacherSpi.saveAllStatus(statusList)
            }

            StatusType.PICNIC_REJECT -> {
                val statusList = request.userIdList.map { userId ->
                    val user = userList.findUserByStudentId(userId)
                        ?: throw UserNotFoundException
                    val status = todayAwaitStatusList.find { user.id == it.studentId }
                        ?: throw StatusNotFoundException

                    status.changePicnicStatus(
                        studentId = user.id,
                        teacherId = teacherId,
                        type = StatusType.PICNIC_REJECT,
                    )
                }

                statusCommandTeacherSpi.saveAllStatus(statusList)
            }

            else -> throw StatusNotFoundException
        }
    }

    override fun queryMyPicnicEndTime(): QueryMyPicnicEndTimeResponse {
        val userInfo = userSpi.queryUserInfoByUserId(userSpi.getCurrentUserId())
        val picnicUserStatus = queryStatusSpi.queryPicnicStudentByStudentIdAndToday(userInfo.id)
            ?: throw StatusNotFoundException
        val endTime = getTodayTimeByPeriod(picnicUserStatus.endPeriod)?.endTime
            ?: throw TimeNotFoundException

        checkIsOverEndTime(endTime)

        return QueryMyPicnicEndTimeResponse(
            userId = userInfo.id,
            name = userInfo.name,
            endTime = endTime,
        )
    }

    override fun queryMyPicnicInfo(): QueryMyPicnicInfoResponse {
        val userInfo = userSpi.queryUserInfoByUserId(userSpi.getCurrentUserId())
        val picnicUserStatus = queryStatusSpi.queryPicnicStudentByStudentIdAndToday(userInfo.id)
            ?: throw StatusNotFoundException
        val endTime = getTodayTimeByPeriod(picnicUserStatus.endPeriod)?.endTime
            ?: throw TimeNotFoundException

        checkIsOverEndTime(endTime)

        val startTime = getTodayTimeByPeriod(picnicUserStatus.startPeriod)?.startTime
            ?: throw TimeNotFoundException

        val application = queryApplicationSpi.queryApplicationByStudentIdAndStatusId(
            studentId = userInfo.id,
            statusId = picnicUserStatus.id,
        ) ?: throw ApplicationNotFoundException

        val teacherName = userSpi.queryUserInfoByUserId(picnicUserStatus.teacherId).name

        return QueryMyPicnicInfoResponse(
            profileFileName = userInfo.profileFileName,
            studentNumber = "${userInfo.grade}${userInfo.classNum}${userInfo.paddedUserNum()}",
            studentName = userInfo.name,
            startTime = startTime,
            endTime = endTime,
            reason = application.reason,
            teacherName = teacherName,
            picnicDate = picnicUserStatus.date,
        )
    }

    private fun checkIsOverEndTime(endTime: LocalTime) {
        if (LocalTime.now().isAfter(endTime)) {
            throw OverEndTimeException
        }
    }

    private fun User.paddedUserNum(): String =
        this.num.toString().padStart(2, '0')

    private fun getTodayTimeByPeriod(period: Int): Time.DomainTimeElement? =
        timeQueryTeacherSpi.queryTime(LocalDate.now()).timeList
            .find { time -> time.period == period }

    private fun List<User>.findUserByStudentId(studentId: UUID): User? =
        this.find { it.id == studentId }

    private fun movementStudent(status: Status?): String {
        var moveClassroomName = ""
        if (status?.type == StatusType.MOVEMENT) {
            val classroomMovement = queryClassroomMovementSpi.queryClassroomMovementByStatus(status)
                ?: throw StatusNotFoundException
            val moveClassroom = queryClassroomSpi.queryClassroomById(classroomMovement.classroomId)
                ?: throw ClassroomNotFoundException
            moveClassroomName = moveClassroom.name
        }
        return moveClassroomName
    }
}
