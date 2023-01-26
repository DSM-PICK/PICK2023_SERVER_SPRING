package com.pickdsm.pickserverspring.domain.application.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.api.QueryPicnicApplicationListApi
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationElement
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.application.spi.QueryApplicationSpi
import com.pickdsm.pickserverspring.domain.user.dto.UserInfo
import com.pickdsm.pickserverspring.domain.user.spi.ApplicationUserSpi
import java.util.*

@ReadOnlyUseCase
class QueryPicnicApplicationListUseCase(
    private val queryApplicationSpi: QueryApplicationSpi,
    private val applicationUserSpi: ApplicationUserSpi,
) : QueryPicnicApplicationListApi {

    override fun getPicnicApplicationList(grade: Int?, classNum: Int?): QueryPicnicApplicationList {
        val userIdList = queryApplicationSpi.queryAllStudentId()

        val userMap = applicationUserSpi.queryUserInfo(userIdList).associateBy { it.id }

        val outingList = queryApplicationSpi.queryPicnicApplicationList()

        if (grade != null && classNum == null) {
            getFilteringByGrade(userMap, outingList, grade)
        } else if (grade == null && classNum != null) {
            getFilteringByClassNum(userMap, outingList, classNum)
        } else if (grade != null && classNum != null) {
            getFilteringByGradeAndClassNum(userMap, outingList, grade, classNum)
        }

        return QueryPicnicApplicationList(
            outing = outingList
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

    private fun getFilteringByGrade(
        userMap: Map<UUID, UserInfo>,
        outingList: List<Application>,
        grade: Int,
    ) {
        val userInfoList = getUserInfo(userMap)
        userInfoList.filter { it.grade == grade }
        filteringByUserMap(userInfoList, outingList)
    }

    private fun getFilteringByClassNum(
        userMap: Map<UUID, UserInfo>,
        outingList: List<Application>,
        classNum: Int,
    ) {
        val userInfoList = getUserInfo(userMap)
        userInfoList.filter { it.classNum == classNum }
        filteringByUserMap(userInfoList, outingList)
    }

    private fun getFilteringByGradeAndClassNum(
        userMap: Map<UUID, UserInfo>,
        outingList: List<Application>,
        grade: Int,
        classNum: Int,
    ) {
        val userInfoList = getUserInfo(userMap)
        userInfoList.filter { it.grade == grade && it.classNum == classNum }
        filteringByUserMap(userInfoList, outingList)
    }

    private fun filteringByUserMap(
        userInfoList: List<UserInfo>,
        outingList: List<Application>,
    ) {
        val userHashMap = userInfoList.associateBy { it.id }
        outingList.filter { it.studentId.equals(userHashMap.keys) }
    }

    private fun getUserInfo(userMap: Map<UUID, UserInfo>): List<UserInfo> =
        applicationUserSpi.queryUserInfo(userMap.keys.toList())
}
