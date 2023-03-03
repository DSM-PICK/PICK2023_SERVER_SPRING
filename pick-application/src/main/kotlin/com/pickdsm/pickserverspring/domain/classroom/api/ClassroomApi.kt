package com.pickdsm.pickserverspring.domain.classroom.api

import com.pickdsm.pickserverspring.domain.classroom.ClassroomType
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryResponsibleClassroomList

interface ClassroomApi {

    fun queryClassroomList(floor: Int, type: ClassroomType): QueryClassroomList

    fun queryResponsibleClassroomList(): QueryResponsibleClassroomList
}
