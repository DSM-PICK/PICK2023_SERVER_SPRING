package com.pickdsm.pickserverspring.domain.application.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.api.ApplicationApi
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationUserIdsRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationElement
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.application.spi.*
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate
import java.util.*

@UseCase
class ApplicationUseCase(
    private val commandApplicationSpi: CommandApplicationSpi,
    private val queryApplicationSpi: QueryApplicationSpi,
    private val commandStatusSpi: CommandStatusSpi,
    private val queryStatusSpi: QueryStatusSpi,
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
        val today = LocalDate.now()

        val currentUserIdList = queryApplicationSpi.queryAllStudentIdByToday(today)

        val userList = userQueryApplicationSpi.queryUserInfo(currentUserIdList)

        val todayOutingList = queryApplicationSpi.queryPicnicApplicationListByToday(today)

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

                val studentNumber = "${user?.grade ?: 0}${user?.classNum ?: 0}${user?.num ?: 0}"

                val studentName = user?.name ?: ""

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

    override fun permitPicnicApplication(request: DomainApplicationUserIdsRequest) {
        val userIdList = request.userIdList

        val applicationIdList = queryApplicationSpi.queryApplicationIdList()

        val statusIdList = queryStatusSpi.queryStatusIdList()

        applicationIdList.filter { applicationIdList.containsAll(userIdList) }

        statusIdList.filter { statusIdList.containsAll(userIdList) }

        commandApplicationSpi.changePermission(applicationIdList)

        commandStatusSpi.changeStatusToPicnic(statusIdList)
    }
}
