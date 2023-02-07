package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi

@ReadOnlyUseCase
class ClassroomUseCase(
    private val queryClassroomSpi: QueryClassroomSpi,
    private val userSpi: UserSpi,
    private val querySelfStudyDirectorSpi: QuerySelfStudyDirectorSpi,
) : ClassroomApi {

    override fun queryClassroomList(floor: Int): QueryClassroomList {
        val classroomList = queryClassroomSpi.queryClassroomListByFloor(floor)

        return QueryClassroomList(classroomList)
    }

    override fun responsibleClassroomList(): QueryClassroomList {
        val teacherId = userSpi.getCurrentUserId()

        val floor = querySelfStudyDirectorSpi.queryResponsibleFloorByTeacherId(teacherId)
            ?: throw UserNotFoundException

        val classroomList = queryClassroomSpi.queryResponsibleClassroomListByFloor(floor)

        return QueryClassroomList(classroomList)
    }
}
