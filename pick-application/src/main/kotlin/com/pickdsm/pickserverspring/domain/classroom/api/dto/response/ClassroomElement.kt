package com.pickdsm.pickserverspring.domain.classroom.api.dto.response

import java.util.UUID

data class ClassroomElement(

    val id: UUID,

    val name: String,

    val description: String,
)
