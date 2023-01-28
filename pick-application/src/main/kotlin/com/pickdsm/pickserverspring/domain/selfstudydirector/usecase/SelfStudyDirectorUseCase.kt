package com.pickdsm.pickserverspring.domain.selfstudydirector.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.SelfStudyDirectorApi
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyElement
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyListResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.UserQuerySelfStudyDirectorSpi
import java.time.LocalDate

@ReadOnlyUseCase
class SelfStudyDirectorUseCase(
    private val querySelfStudyDirectorSpi: QuerySelfStudyDirectorSpi,
    private val userQuerySelfStudyDirectorSpi: UserQuerySelfStudyDirectorSpi,
) : SelfStudyDirectorApi {

    override fun getSelfStudyTeacher(month: String): SelfStudyListResponse {
        val startDate = LocalDate.of(LocalDate.now().year, month.toInt(), 1)
        val studentIdList = querySelfStudyDirectorSpi.querySelfStudyDirectorTeacherIdByDate(startDate)
        val userInfoList = userQuerySelfStudyDirectorSpi.queryUserInfo(studentIdList)
        val selfStudyDirectorList = querySelfStudyDirectorSpi.querySelfStudyDirectorByDate(startDate)

        // 해당 달의 1일부터 마지막일까지 반복문을 돌면서 값 가공
        val selfStudyDirectorResponseList = (1..startDate.lengthOfMonth())
            .filter { i ->
                selfStudyDirectorList.find {
                    it.date != LocalDate.of(startDate.year, startDate.month, i)
                } == null
            }
            .map { i ->
                val date = LocalDate.of(startDate.year, startDate.month, i)
                val type = selfStudyDirectorList.find { it.date == date }?.type ?: DirectorType.SELF_STUDY
                val teacher = MutableList(5) { "" }

                selfStudyDirectorList.filter { it.date == date }.map { selfStudy ->
                    teacher[selfStudy.floor - 1] = userInfoList.find { it.id == selfStudy.teacherId }?.name ?: ""
                }

                SelfStudyElement(type = type, date = date, teacher = teacher)
            }

        return SelfStudyListResponse(selfStudyDirectorResponseList)
    }
}
