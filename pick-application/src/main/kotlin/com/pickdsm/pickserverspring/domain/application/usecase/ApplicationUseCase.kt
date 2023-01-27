package com.pickdsm.pickserverspring.domain.application.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.api.ApplicationApi
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationElement
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.application.spi.CommandApplicationSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryApplicationSpi
import com.pickdsm.pickserverspring.domain.user.dto.UserInfo
import com.pickdsm.pickserverspring.domain.user.spi.ApplicationUserSpi
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate
import java.util.*

@UseCase
class ApplicationUseCase(
    private val commandApplicationSpi: CommandApplicationSpi,
    private val queryApplicationSpi: QueryApplicationSpi,
    private val applicationUserSpi: ApplicationUserSpi,
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

    override fun getPicnicApplicationListByGradeAndClassNum(grade: Int?, classNum: Int?): QueryPicnicApplicationList {
        val currentUserIdList = queryApplicationSpi.queryAllStudentId()

        val userMap = applicationUserSpi.queryUserInfo(currentUserIdList).associateBy { it.id }

        val todayOutingList = queryApplicationSpi.queryPicnicApplicationListByToday(LocalDate.now())

        if (grade != null && classNum == null) {
            filteringByGrade(userMap, todayOutingList, grade)
        } else if (grade == null && classNum != null) {
            filteringByClassNum(userMap, todayOutingList, classNum)
        } else if (grade != null && classNum != null) {
            filteringByGradeAndClassNum(userMap, todayOutingList, grade, classNum)
        }

        return QueryPicnicApplicationList(
            outing = todayOutingList
                .map { application ->
                    QueryPicnicApplicationElement(
                        startTime = application.startTime,
                        endTime = application.endTime,
                        reason = application.reason,
                        studentId = application.studentId,
                        userMap = userMap,
                    )
                },
        )
    }

    private fun filteringByGrade(
        userMap: Map<UUID, UserInfo>,
        todayOutingList: List<Application>,
        grade: Int,
    ) {
        val checkedUserList = userMap.values.filter { it.grade == grade }
        val userInfoList = filteringUserList(checkedUserList)
        filteringByUserMap(userInfoList, todayOutingList)
    }

    private fun filteringByClassNum(
        userMap: Map<UUID, UserInfo>,
        todayOutingList: List<Application>,
        classNum: Int,
    ) {
        val checkedUserList = userMap.values.filter { it.classNum == classNum }
        val userInfoList = filteringUserList(checkedUserList)
        filteringByUserMap(userInfoList, todayOutingList)
    }

    private fun filteringByGradeAndClassNum(
        userMap: Map<UUID, UserInfo>,
        toadyOutingList: List<Application>,
        grade: Int,
        classNum: Int,
    ) {
        val checkedUserList = userMap.values.filter { it.grade == grade && it.classNum == classNum }
        val userInfoList = filteringUserList(checkedUserList)
        filteringByUserMap(userInfoList, toadyOutingList)
    }

    private fun getUserInfo(userMap: Map<UUID, UserInfo>): List<UserInfo> =
        applicationUserSpi.queryUserInfo(userMap.keys.toList())

    private fun filteringUserList(checkedUserList: List<UserInfo>): List<UserInfo> {
        val checkedUserMap = checkedUserList.associateBy { it.id }
        return getUserInfo(checkedUserMap)
    }

    private fun filteringByUserMap(
        userInfoList: List<UserInfo>,
        todayOutingList: List<Application>,
    ) {
        val userHashMap = userInfoList.associateBy { it.id }
        todayOutingList.filter { it.studentId.equals(userHashMap.keys) }
    }
}
