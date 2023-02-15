package com.pickdsm.pickserverspring.domain.classroom.api

import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList

interface ClassroomApi {

    fun queryClassroomList(floor: Int, type: String): QueryClassroomList

    fun queryResponsibleClassroomList(): QueryClassroomList
}
