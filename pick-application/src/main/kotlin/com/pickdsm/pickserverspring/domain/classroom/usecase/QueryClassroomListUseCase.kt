package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.classroom.api.QueryClassroomListApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi

@ReadOnlyUseCase
class QueryClassroomListUseCase(
    private val queryClassroomSpi: QueryClassroomSpi,
) : QueryClassroomListApi {

    override fun queryClassroomList(floor: Int): QueryClassroomList {
        val classroomList = queryClassroomSpi.queryClassroomListByFloor(floor)

        return QueryClassroomList(
            classroomList = classroomList.map { classroom ->
                ClassroomElement(
                    id = classroom.id,
                    name = classroom.name,
                )
            },
        )
    }
}
