package com.pickdsm.pickserverspring.domain.selfstudydirector.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.SelfStudyDirectorApi
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.request.DomainChangeSelfStudyDirectorRequest
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.request.DomainDeleteSelfStudyDirectorRequest
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.request.DomainRegisterSelfStudyDirectorRequest
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
import com.pickdsm.pickserverspring.domain.user.dto.request.UserInfoRequest
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate

@ReadOnlyUseCase
class SelfStudyDirectorUseCase(
    private val querySelfStudyDirectorSpi: QuerySelfStudyDirectorSpi,
    private val userQuerySelfStudyDirectorSpi: UserQuerySelfStudyDirectorSpi,
    private val queryTypeSpi: QueryTypeSpi,
    private val userSpi: UserSpi,
    private val commandSelfStudyDirectorSpi: CommandSelfStudyDirectorSpi,
) : SelfStudyDirectorApi {

    override fun getSelfStudyTeacher(month: String): SelfStudyListResponse {
        val startDate = LocalDate.of(LocalDate.now().year, month.toInt(), 1)
        val selfStudyDirectorList = querySelfStudyDirectorSpi.querySelfStudyDirectorByDate(startDate)
        val teacherIdList = selfStudyDirectorList.map { it.teacherId }
        val userIdRequest = UserInfoRequest(teacherIdList)
        val userInfoList = userQuerySelfStudyDirectorSpi.queryUserInfo(userIdRequest)
        val typeList = queryTypeSpi.queryTypeListByDate(startDate)

        // 해당 달의 1일부터 마지막일까지 반복문을 돌면서 값 가공
        val selfStudyDirectorResponseList = (1..startDate.lengthOfMonth())
            .map { i ->
                val date = LocalDate.of(startDate.year, startDate.month, i)
                val directorType = typeList.find { it.date == date }?.type ?: DirectorType.SELF_STUDY
                val teacher = MutableList(5) { "" }

                selfStudyDirectorList.filter {
                    val type = typeList.find { type -> type.id == it.typeId } ?: throw TypeNotFoundException
                    type.date == date
                }.map { selfStudy ->
                    teacher[selfStudy.floor - 1] = userInfoList.find { it.id == selfStudy.teacherId }?.name ?: ""
                }

                SelfStudyElement(type = directorType, date = date, teacher = teacher)
            }

        return SelfStudyListResponse(selfStudyDirectorResponseList)
    }

    override fun getTodaySelfStudyTeacher(): TodaySelfStudyTeacherResponse {
        val selfStudyList = querySelfStudyDirectorSpi.querySelfStudyDirectorByToday()
        val teacherIdList = selfStudyList.map { it.teacherId }
        val userIdRequest = UserInfoRequest(teacherIdList)
        val teacherList = userQuerySelfStudyDirectorSpi.queryUserInfo(userIdRequest)

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
        val teacher = userSpi.queryUserInfoByUserId(teacherId)
        val selfStudy = querySelfStudyDirectorSpi.queryAllSelfStudyDirectorByTeacherIdAndDate(teacher.id, date)
        return SelfStudyStateResponse(
            date = date,
            name = teacher.name,
            floor = selfStudy.map(SelfStudyDirector::floor),
        )
    }

    override fun blockMoveClassroom() {
        val teacherId = userSpi.getCurrentUserId()
        val teacher = querySelfStudyDirectorSpi.querySelfStudyDirectorByTeacherId(teacherId)

        commandSelfStudyDirectorSpi.setRestrictionMovementTrue(teacher)
        // TODO 이동 제한시 동아리, 자습일 떄 구분해서 이동한 학생 상태 지우기 추가해야함
    }

    override fun changeSelfStudyDirector(request: DomainChangeSelfStudyDirectorRequest) {
        val selfStudyDirector =
            querySelfStudyDirectorSpi.querySelfStudyDirectorByDateAndFloor(request.date, request.floor)
                ?: throw SelfStudyDirectorNotFoundException

        commandSelfStudyDirectorSpi.saveSelfStudyDirector(
            selfStudyDirector.changeSelfStudyDirector(teacherId = request.teacherId),
        )
    }

    override fun blockUnlockMoveClassroom() {
        val teacherId = userSpi.getCurrentUserId()
        val teacher = querySelfStudyDirectorSpi.querySelfStudyDirectorByTeacherId(teacherId)

        commandSelfStudyDirectorSpi.saveSelfStudyDirector(teacher.setBlockClassroomMovementFalse())
    }

    override fun registerSelfStudyDirector(request: DomainRegisterSelfStudyDirectorRequest) {
        val type = queryTypeSpi.queryTypeByDate(request.date)
            ?: throw TypeNotFoundException
        val selfStudyDirector = SelfStudyDirector(
            floor = request.floor,
            teacherId = request.teacherId,
            typeId = type.id,
        )

        commandSelfStudyDirectorSpi.saveSelfStudyDirector(
            selfStudyDirector,
        )
    }

    override fun deleteSelfStudyDirector(request: DomainDeleteSelfStudyDirectorRequest) {
        val selfStudyDirector =
            querySelfStudyDirectorSpi.querySelfStudyDirectorByDateAndFloor(request.date, request.floor)
                ?: throw SelfStudyDirectorNotFoundException

        commandSelfStudyDirectorSpi.deleteSelfStudyDirector(
            selfStudyDirector,
        )
    }
}
