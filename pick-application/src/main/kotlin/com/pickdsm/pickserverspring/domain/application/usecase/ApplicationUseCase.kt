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
import com.pickdsm.pickserverspring.domain.application.exception.AlreadyPicnicAwaitException
import com.pickdsm.pickserverspring.domain.application.exception.ApplicationNotFoundException
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
    private val queryClubSpi: QueryClubSpi,
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
    private val queryTypeSpi: QueryTypeSpi,
) : ApplicationApi {

    override fun saveApplicationToGoOut(request: DomainApplicationGoOutRequest) {
        val studentId = userSpi.getCurrentUserId()
        val status = Status(
            studentId = studentId,
            teacherId = UUID(0, 0), // TODO: 선생님 아이디 뭐로 넣을지 나중에 정하기
            startPeriod = request.desiredStartPeriod,
            endPeriod = request.desiredEndPeriod,
            type = StatusType.AWAIT,
        )
        
        if(!queryApplicationSpi.queryApplicationByStudentId(studentId)) {
            throw AlreadyPicnicAwaitException
        }

        val saveStatusId = statusCommandTeacherSpi.saveStatusAndGetStatusId(status)
        commandApplicationSpi.saveApplication(
            Application(
                reason = request.reason,
                statusId = saveStatusId,
            ),
        )
    }

    override fun queryPicnicApplicationListByGradeAndClassNum(
        grade: String?,
        classNum: String?,
        floor: Int?,
        type: DirectorType,
    ): QueryPicnicApplicationList {
        val today = LocalDate.now()
        val todayOutingList = queryApplicationSpi.queryPicnicApplicationListByToday(today)
        val todayStatusList = queryStatusSpi.queryAwaitStudentListByToday(today)
        val todayApplicationStudentIdList = todayStatusList.map { it.studentId }
        val timeList = timeQueryTeacherSpi.queryTime(today)
        val userList = userQueryApplicationSpi.queryUserInfo(todayApplicationStudentIdList)

        val outing = mutableListOf<QueryPicnicApplicationElement>()

        when (type) {
            DirectorType.SELF_STUDY -> {
                when {
                    floor == null -> {
                        todayStatusList.filter { status ->
                            val user = userList.find { user -> user.id == status.studentId } ?: return@filter false

                            val studentGrade = grade?.toIntOrNull() ?: 0
                            val studentClassNum = classNum?.toIntOrNull() ?: 0

                            when {
                                studentGrade != 0 && studentClassNum == 0 -> studentGrade == user.grade
                                studentGrade == 0 -> false
                                else -> (studentGrade == user.grade && studentClassNum == user.classNum)
                            }
                        }.map { status ->
                            val user = userList.find { user -> user.id == status.studentId }
                                ?: throw UserNotFoundException
                            val startTime = timeList.timeList.find { time -> time.period == status.startPeriod }
                                ?: throw TimeNotFoundException
                            val endTime = timeList.timeList.find { time -> time.period == status.endPeriod }
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplications = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                                studentName = user.name,
                                startTime = startTime.startTime,
                                endTime = endTime.endTime,
                                reason = application.reason,
                            )

                            outing.add(picnicApplications)
                        }
                    }

                    floor != null -> {
                        todayStatusList.filter { status ->
                            val user = userList.find { user -> user.id == status.studentId } ?: return@filter false
                            val classroomGrade = queryClassroomSpi.queryClassroomGradeByFloor(floor)
                            classroomGrade == user.grade
                        }.map { status ->
                            val user = userList.find { user -> user.id == status.studentId }
                                ?: throw UserNotFoundException
                            val startTime = timeList.timeList.find { time -> time.period == status.startPeriod }
                                ?: throw TimeNotFoundException
                            val endTime = timeList.timeList.find { time -> time.period == status.endPeriod }
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplication = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                                studentName = user.name,
                                startTime = startTime.startTime,
                                endTime = endTime.endTime,
                                reason = application.reason,
                            )

                            outing.add(picnicApplication)
                        }
                    }
                }
            }

            DirectorType.TUE_CLUB, DirectorType.FRI_CLUB -> {
                when {
                    floor == null -> {
                        todayStatusList.filter { status ->
                            val user = userList.find { user -> user.id == status.studentId } ?: return@filter false

                            val studentGrade = grade?.toIntOrNull() ?: 0
                            val studentClassNum = classNum?.toIntOrNull() ?: 0

                            when {
                                studentGrade != 0 && studentClassNum == 0 -> studentGrade == user.grade
                                studentGrade == 0 -> false
                                else -> (studentGrade == user.grade && studentClassNum == user.classNum)
                            }
                        }.map { status ->
                            val user = userList.find { user -> user.id == status.studentId }
                                ?: throw UserNotFoundException
                            val startTime = timeList.timeList.find { time -> time.period == status.startPeriod }
                                ?: throw TimeNotFoundException
                            val endTime = timeList.timeList.find { time -> time.period == status.endPeriod }
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplications = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                                studentName = user.name,
                                startTime = startTime.startTime,
                                endTime = endTime.endTime,
                                reason = application.reason,
                            )

                            outing.add(picnicApplications)
                        }
                    }

                    floor != null -> {
                        todayStatusList.filter { status ->
                            val user = userList.find { user -> user.id == status.studentId } ?: return@filter false
                            val clubStudentIdList =
                                queryClubSpi.queryClubStudentIdListByFloor(floor).find { user.id == it }

                            clubStudentIdList == user.id
                        }.map { status ->
                            val user = userList.find { user -> user.id == status.studentId }
                                ?: throw UserNotFoundException
                            val startTime = timeList.timeList.find { time -> time.period == status.startPeriod }
                                ?: throw TimeNotFoundException
                            val endTime = timeList.timeList.find { time -> time.period == status.endPeriod }
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplications = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                                studentName = user.name,
                                startTime = startTime.startTime,
                                endTime = endTime.endTime,
                                reason = application.reason,
                            )

                            outing.add(picnicApplications)
                        }
                    }
                }
            }

            DirectorType.AFTER_SCHOOL -> {
                when {
                    floor == null -> {
                        todayStatusList.filter { status ->
                            val user = userList.find { user -> user.id == status.studentId } ?: return@filter false

                            val studentGrade = grade?.toIntOrNull() ?: 0
                            val studentClassNum = classNum?.toIntOrNull() ?: 0

                            when {
                                studentGrade != 0 && studentClassNum == 0 -> studentGrade == user.grade
                                studentGrade == 0 -> false
                                else -> (studentGrade == user.grade && studentClassNum == user.classNum)
                            }
                        }.map { status ->
                            val user = userList.find { user -> user.id == status.studentId }
                                ?: throw UserNotFoundException
                            val startTime = timeList.timeList.find { time -> time.period == status.startPeriod }
                                ?: throw TimeNotFoundException
                            val endTime = timeList.timeList.find { time -> time.period == status.endPeriod }
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplications = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                                studentName = user.name,
                                startTime = startTime.startTime,
                                endTime = endTime.endTime,
                                reason = application.reason,
                            )

                            outing.add(picnicApplications)
                        }
                    }

                    floor != null -> {
                        todayStatusList.filter { status ->
                            val user = userList.find { user -> user.id == status.studentId } ?: return@filter false
                            val afterSchoolStudentList =
                                queryAfterSchoolSpi.queryAfterSchoolStudentIdByFloor(floor).find { user.id == it }

                            afterSchoolStudentList == user.id
                        }.map { status ->
                            val user = userList.find { user -> user.id == status.studentId }
                                ?: throw UserNotFoundException
                            val startTime = timeList.timeList.find { time -> time.period == status.startPeriod }
                                ?: throw TimeNotFoundException
                            val endTime = timeList.timeList.find { time -> time.period == status.endPeriod }
                                ?: throw TimeNotFoundException
                            val application = todayOutingList.find { application -> application.statusId == status.id }
                                ?: throw ApplicationNotFoundException

                            val picnicApplications = QueryPicnicApplicationElement(
                                studentId = user.id,
                                studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                                studentName = user.name,
                                startTime = startTime.startTime,
                                endTime = endTime.endTime,
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

    override fun getAllStudentStatusByClassroomId(classroomId: UUID): QueryStudentStatusList {
        val dateType = queryTypeSpi.queryDirectorTypeByDate(LocalDate.now()) ?: DirectorType.SELF_STUDY
        val todayStudentStatusList = queryStatusSpi.queryStatusListByToday()
        val classroom = queryClassroomSpi.queryClassroomById(classroomId) ?: throw ClassroomNotFoundException
        val students = mutableListOf<QueryStudentStatusElement>()

        when (dateType) {
            DirectorType.AFTER_SCHOOL -> {
                val afterSchoolList = queryAfterSchoolSpi.queryAfterSchoolListByClassroomId(classroomId)
                val afterSchoolStudentIdList = afterSchoolList.map { it.studentId }
                val afterSchoolUserInfos = userSpi.queryUserInfo(afterSchoolStudentIdList)

                afterSchoolUserInfos.map { user ->
                    val status = todayStudentStatusList.find { user.id == it.studentId }
                    val movementClassroomName = movementStudent(status)

                    val studentStatus = QueryStudentStatusElement(
                        studentId = user.id,
                        studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
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
                val clubUserInfos = userSpi.queryUserInfo(clubStudentIdList)

                clubUserInfos.map { user ->
                    val status = todayStudentStatusList.find { user.id == it.studentId }
                    val movementClassroomName = movementStudent(status)

                    val studentStatus = QueryStudentStatusElement(
                        studentId = user.id,
                        studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
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
        val userList = userSpi.queryUserInfo(request.userIdList)

        val statusList = request.userIdList.map {
            val user = userList.find { user -> user.id == it }
                ?: throw UserNotFoundException

            Status(
                studentId = user.id,
                teacherId = teacherId,
                startPeriod = request.startPeriod,
                endPeriod = request.endPeriod,
                type = StatusType.PICNIC,
            )
        }

        statusCommandTeacherSpi.saveAllStatus(statusList)
    }

    override fun savePicnicAcceptOrRefuse(request: DomainPicnicAcceptOrRefuseRequest) {
        val teacherId = userSpi.getCurrentUserId()
        val userList = userSpi.queryUserInfo(request.userIdList)
        val todayAwaitStatusList = queryStatusSpi.queryAwaitStudentListByToday(LocalDate.now())

        when (request.type) {
            StatusType.PICNIC -> {
                val statusList = request.userIdList.map {
                    val user = userList.find { user -> user.id == it }
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
                val statusList = request.userIdList.map {
                    val user = userList.find { user -> user.id == it }
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

    override fun getMyPicnicEndTime(): QueryMyPicnicEndTimeResponse {
        val userInfo = userSpi.queryUserInfoByUserId(userSpi.getCurrentUserId())
        val picnicUserStatus = queryStatusSpi.queryPicnicStudentByStudentIdAndToday(userInfo.id)
            ?: throw StatusNotFoundException
        val endTime = timeQueryTeacherSpi.queryTime(LocalDate.now())
            .timeList.find { time -> time.period == picnicUserStatus.endPeriod }?.endTime
            ?: throw TimeNotFoundException

        return QueryMyPicnicEndTimeResponse(
            userId = userInfo.id,
            name = userInfo.name,
            endTime = endTime,
        )
    }

    override fun getMyPicnicInfo(): QueryMyPicnicInfoResponse {
        val userInfo = userSpi.queryUserInfoByUserId(userSpi.getCurrentUserId())
        val picnicUserStatus = queryStatusSpi.queryPicnicStudentByStudentIdAndToday(userInfo.id)
            ?: throw StatusNotFoundException
        val teacherName = userSpi.queryUserInfoByUserId(picnicUserStatus.teacherId).name

        val application = queryApplicationSpi.queryApplicationByStudentIdAndStatusId(
            studentId = userInfo.id,
            statusId = picnicUserStatus.id,
        ) ?: throw ApplicationNotFoundException

        val startTime = timeQueryTeacherSpi.queryTime(LocalDate.now())
            .timeList.find { time -> time.period == picnicUserStatus.startPeriod }?.startTime
            ?: throw TimeNotFoundException
        val endTime = timeQueryTeacherSpi.queryTime(LocalDate.now())
            .timeList.find { time -> time.period == picnicUserStatus.endPeriod }?.endTime
            ?: throw TimeNotFoundException

        return QueryMyPicnicInfoResponse(
            profileFileName = userInfo.profileFileName,
            studentNumber = "${userInfo.grade}${userInfo.classNum}${checkUserNumLessThanTen(userInfo.num)}",
            studentName = userInfo.name,
            startTime = startTime,
            endTime = endTime,
            reason = application.reason,
            teacherName = teacherName,
        )
    }

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

    private fun checkUserNumLessThanTen(userNum: Int) =
        if (userNum < 10) {
            "0$userNum"
        } else {
            userNum.toString()
        }
}
