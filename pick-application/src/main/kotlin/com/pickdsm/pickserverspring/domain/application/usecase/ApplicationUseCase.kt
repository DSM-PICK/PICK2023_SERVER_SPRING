package com.pickdsm.pickserverspring.domain.application.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.api.ApplicationApi
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationUserIdsRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationElement
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicStudentElement
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicStudentList
import com.pickdsm.pickserverspring.domain.application.exception.ApplicationNotFoundException
import com.pickdsm.pickserverspring.domain.application.spi.CommandApplicationSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryApplicationSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.application.spi.UserQueryApplicationSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.UserQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate
import java.util.UUID

@UseCase
class ApplicationUseCase(
    private val statusCommandTeacherSpi: StatusCommandTeacherSpi,
    private val commandApplicationSpi: CommandApplicationSpi,
    private val queryApplicationSpi: QueryApplicationSpi,
    private val queryStatusSpi: QueryStatusSpi,
    private val userQueryTeacherSpi: UserQueryTeacherSpi,
    private val userQueryApplicationSpi: UserQueryApplicationSpi,
    private val userSpi: UserSpi,
) : ApplicationApi {

    override fun saveApplicationToGoOut(request: DomainApplicationGoOutRequest) {
        val studentId: UUID = userSpi.getCurrentUserId()
        val application = Application(
            studentId = studentId,
            date = LocalDate.now(),
            startTime = request.startTime,
            endTime = request.endTime,
            reason = request.reason,
        )
        commandApplicationSpi.saveApplication(application)
    }

    override fun queryPicnicApplicationListByGradeAndClassNum(
        grade: String,
        classNum: String,
    ): QueryPicnicApplicationList {
        val todayOutingList = queryApplicationSpi.queryPicnicApplicationListByToday(LocalDate.now())

        val todayApplicationStudentIdList = todayOutingList.map { application -> application.studentId }

        val userList = userQueryApplicationSpi.queryUserInfo(todayApplicationStudentIdList)

        val outing: List<QueryPicnicApplicationElement> = todayOutingList
            .filter { application ->
                val user = userList.find { user -> user.id == application.studentId } ?: return@filter false

                val studentGrade = grade.toIntOrNull() ?: 0
                val studentClassNum = classNum.toIntOrNull() ?: 0

                when {
                    studentGrade != 0 && studentClassNum == 0 -> studentGrade == user.grade
                    studentGrade == 0 -> false
                    else -> (studentGrade == user.grade && studentClassNum == user.classNum)
                }
            }
            .map { application ->
                val user = userList.find { user -> user.id == application.studentId }
                    ?: throw UserNotFoundException

                val studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}"

                val studentName = user.name

                QueryPicnicApplicationElement(
                    studentId = application.studentId,
                    studentNumber = studentNumber,
                    studentName = studentName,
                    startTime = application.startTime,
                    endTime = application.endTime,
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

    override fun permitPicnicApplication(request: DomainApplicationUserIdsRequest) {
        val userIdList = request.userIdList

        val teacherId = userSpi.getCurrentUserId()

        val todayApplicationList = queryApplicationSpi.queryApplicationListByToday(LocalDate.now())

        val applicationIdList = todayApplicationList.map { application -> application.id }

        val userList = userQueryTeacherSpi.queryUserInfo(userIdList)

        val statusList = userIdList.map {
            val user = userList.find { user -> user.id == it }
                ?: throw UserNotFoundException

            val application = todayApplicationList.find { application -> application.studentId == it }
                ?: throw ApplicationNotFoundException

            Status(
                studentId = user.id,
                teacherId = teacherId,
                type = StatusType.PICNIC,
                date = LocalDate.now(),
                startTime = application.startTime,
                endTime = application.endTime,
            )
        }

        commandApplicationSpi.changePermission(applicationIdList)

        statusCommandTeacherSpi.saveAllStatus(statusList)
    }

    private fun checkUserNumLessThanTen(userNum: Int) =
        if (userNum < 10) {
            "0$userNum"
        } else {
            userNum.toString()
        }
}
