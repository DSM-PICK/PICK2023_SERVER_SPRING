package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.classroom.ClassroomType
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import com.pickdsm.pickserverspring.domain.classroom.exception.FloorNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.TypeNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QueryTypeSpi
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi

@ReadOnlyUseCase
class ClassroomUseCase(
    private val userSpi: UserSpi,
    private val queryClassroomSpi: QueryClassroomSpi,
    private val querySelfStudyDirectorSpi: QuerySelfStudyDirectorSpi,
    private val queryClubSpi: QueryClubSpi,
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
    private val queryTypeSpi: QueryTypeSpi,
) : ClassroomApi {

    override fun queryClassroomList(floor: Int, type: ClassroomType): QueryClassroomList {
        val classroomList = when (type.name) {
            ClassroomType.SELF_STUDY.name -> queryClassroomSpi.querySelfStudyClassroomListByFloor(floor)
            ClassroomType.CLUB.name -> queryClubSpi.queryClubClassroomListByFloor(floor)
            ClassroomType.AFTER_SCHOOL.name -> queryAfterSchoolSpi.queryAfterSchoolClassroomListByFloor(floor)
            ClassroomType.ALL.name -> queryClassroomSpi.queryAllClassroomListByFloor(floor)
            else -> throw TypeNotFoundException
        }

        val response = classroomList.map {
            ClassroomElement(
                id = it.id,
                name = it.name,
                description = it.description,
            )
        }

        return QueryClassroomList(response)
    }

    override fun queryResponsibleClassroomList(): QueryClassroomList {
        val teacherId = userSpi.getCurrentUserId()
        val floor = querySelfStudyDirectorSpi.queryResponsibleFloorByTeacherId(teacherId)
            ?: throw FloorNotFoundException
        val todayType = queryTypeSpi.queryTypeByToday()

        val classroomList = when (todayType?.type?.name) {
            ClassroomType.SELF_STUDY.name -> queryClassroomSpi.querySelfStudyClassroomListByFloor(floor)
            ClassroomType.CLUB.name -> queryClubSpi.queryClubClassroomListByFloor(floor)
            ClassroomType.AFTER_SCHOOL.name -> queryAfterSchoolSpi.queryAfterSchoolClassroomListByFloor(floor)
            else -> throw TypeNotFoundException
        }

        val response = classroomList.map {
            ClassroomElement(
                id = it.id,
                name = it.name,
                description = it.description,
            )
        }

        return QueryClassroomList(response)
    }
}
