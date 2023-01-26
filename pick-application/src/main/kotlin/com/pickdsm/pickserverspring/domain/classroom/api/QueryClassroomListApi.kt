package com.pickdsm.pickserverspring.domain.classroom.api

import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList

interface QueryClassroomListApi {

    fun queryClassroomList(floor: Int): QueryClassroomList
}
