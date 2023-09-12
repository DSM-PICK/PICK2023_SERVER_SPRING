package com.pickdsm.pickserverspring.domain.classroom.api.dto.response

import java.util.UUID

data class QueryResponsibleClassroomList(
    val floor: Int,
    val responsibleClassroomList: List<ResponsibleClassroomElement>,
) {
    data class ResponsibleClassroomElement(
        val id: UUID,
        val name: String,
        val description: String,
        val isUserExist: Boolean,
    )
}
