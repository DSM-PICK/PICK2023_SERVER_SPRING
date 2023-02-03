package com.pickdsm.pickserverspring.domain.selfstudydirector.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.SelfStudyDirectorApi
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyElement
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyListResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.TodaySelfStudyTeacherResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.UserQuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.user.User
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

    override fun getTodaySelfStudyTeacher(): TodaySelfStudyTeacherResponse {
        val selfStudyList = querySelfStudyDirectorSpi.querySelfStudyDirectorByDate(LocalDate.now())
        val teacherIdList = selfStudyList.map { it.teacherId }
        val teacherList = userQuerySelfStudyDirectorSpi.queryUserInfo(teacherIdList)

        return TodaySelfStudyTeacherResponse(
            secondFloor = getTeacherName(teacherList, selfStudyList, 2),
            thirdFloor = getTeacherName(teacherList, selfStudyList, 3),
            fourthFloor = getTeacherName(teacherList, selfStudyList, 4),
        )
    }

    private fun getTeacherName(teachers: List<User>, selfStudies: List<SelfStudyDirector>, floor: Int): String {
        val teacherId = selfStudies.find { it.floor == floor }?.teacherId ?: return ""
        return teachers.find { it.id == teacherId }?.name ?: ""
    }
}
