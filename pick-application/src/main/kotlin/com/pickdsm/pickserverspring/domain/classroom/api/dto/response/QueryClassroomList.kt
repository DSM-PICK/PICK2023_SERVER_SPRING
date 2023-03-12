package com.pickdsm.pickserverspring.domain.classroom.api.dto.response

import java.util.UUID

data class QueryClassroomList(
    val classroomList: List<ClassroomElement>,
) {
    data class ClassroomElement(
        val id: UUID,
        val typeId: UUID,
        val name: String,
        val description: String,
    )
}
