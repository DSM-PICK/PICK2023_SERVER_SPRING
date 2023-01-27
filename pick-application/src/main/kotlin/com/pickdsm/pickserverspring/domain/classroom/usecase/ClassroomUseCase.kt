package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi

@ReadOnlyUseCase
class ClassroomUseCase(
    private val queryClassroomSpi: QueryClassroomSpi,
) : ClassroomApi {

    override fun queryClassroomList(floor: Int): QueryClassroomList {
        val classroomList = queryClassroomSpi.queryClassroomListByFloor(floor)

        return QueryClassroomList(classroomList)
    }
}
