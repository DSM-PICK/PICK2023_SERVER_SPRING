package com.pickdsm.pickserverspring.domain.classroom.api.dto.request

import java.util.UUID

data class DomainClassroomMovementRequest(
    val classroomId: UUID,
    val period: Int,
)
