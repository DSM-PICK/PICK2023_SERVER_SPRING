package com.pickdsm.pickserverspring.domain.selfstudydirector.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.application.spi.CommandStatusSpi
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector
import com.pickdsm.pickserverspring.domain.selfstudydirector.Type
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.SelfStudyDirectorApi
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.requst.DomainChangeSelfStudyDirectorRequest
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyElement
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyListResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyStateResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.TodaySelfStudyTeacherResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.SelfStudyDirectorNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.TypeNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.CommandSelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QueryTypeSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.UserQuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate

@ReadOnlyUseCase
class SelfStudyDirectorUseCase(
    private val querySelfStudyDirectorSpi: QuerySelfStudyDirectorSpi,
    private val userQuerySelfStudyDirectorSpi: UserQuerySelfStudyDirectorSpi,
    private val queryTypeSpi: QueryTypeSpi,
    private val userSpi: UserSpi,
    private val commandSelfStudyDirectorSpi: CommandSelfStudyDirectorSpi,
    private val queryStatusSpi: QueryStatusSpi,
    private val commandStatusSpi: CommandStatusSpi,
) : SelfStudyDirectorApi {

    override fun getSelfStudyTeacher(month: String): SelfStudyListResponse {
        val startDate = LocalDate.of(LocalDate.now().year, month.toInt(), 1)
        val selfStudyDirectorList = querySelfStudyDirectorSpi.querySelfStudyDirectorByDate(startDate)
        val teacherIdList = selfStudyDirectorList.map { it.teacherId }
        val userInfoList = userQuerySelfStudyDirectorSpi.queryUserInfo(teacherIdList)
        val typeList = queryTypeSpi.queryTypeListByToday()

        // 해당 달의 1일부터 마지막일까지 반복문을 돌면서 값 가공
        val selfStudyDirectorResponseList = (1..startDate.lengthOfMonth())
            .filter { i ->
                selfStudyDirectorList.find {
                    val type = typeList.find { type -> type.id == it.typeId } ?: throw TypeNotFoundException
                    type.date != LocalDate.of(startDate.year, startDate.month, i)
                } == null
            }
            .map { i ->
                val date = LocalDate.of(startDate.year, startDate.month, i)
                val directorType: DirectorType = typeList.find { it.date == date }?.type ?: DirectorType.SELF_STUDY
                val teacher = MutableList(5) { "" }

                selfStudyDirectorList.filter {
                    val type: Type = typeList.find { type -> type.id == it.typeId } ?: throw TypeNotFoundException
                    type.date == date
                }.map { selfStudy ->
                    teacher[selfStudy.floor - 1] = userInfoList.find { it.id == selfStudy.teacherId }?.name ?: ""
                }

                SelfStudyElement(type = directorType, date = date, teacher = teacher)
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

    override fun getSelfStudyState(): SelfStudyStateResponse {
        val date = LocalDate.now()
        val teacherId = userSpi.getCurrentUserId()
        val teacher = userQuerySelfStudyDirectorSpi.queryUserInfo(listOf(teacherId)).first()
        val selfStudy = querySelfStudyDirectorSpi.queryAllSelfStudyDirectorByTeacherIdAndDate(teacher.id, date)
        if (selfStudy.isEmpty()) {
            throw SelfStudyDirectorNotFoundException
        }
        return SelfStudyStateResponse(
            date = date,
            name = teacher.name,
            floor = selfStudy.map(SelfStudyDirector::floor),
        )
    }

    override fun blockMoveClassroom() {
        val teacherId = userSpi.getCurrentUserId()
        val teacher = querySelfStudyDirectorSpi.querySelfStudyDirectorById(teacherId)

        commandSelfStudyDirectorSpi.setRestrictionMovementTrue(teacher)
        val statusList = queryStatusSpi.queryMovementStudentInfoListByToday(LocalDate.now())

        commandStatusSpi.deleteAllMovementStudent(statusList)
    }

    override fun changeSelfStudyDirector(requset: DomainChangeSelfStudyDirectorRequest) {
        val selfStudyDirector = querySelfStudyDirectorSpi.querySelfStudyDirectorByDateAndFloor(requset.date, requset.floor)
            ?: throw SelfStudyDirectorNotFoundException

        commandSelfStudyDirectorSpi.updateSelfStudyDirector(
            selfStudyDirector.changeSelfStudyDirector(teacherId = requset.teacherId),
        )
    }
}
