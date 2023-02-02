package com.pickdsm.pickserverspring.domain.classroom.api

import com.pickdsm.pickserverspring.domain.classroom.api.dto.request.DomainClassroomMovementRequest
import java.util.UUID

interface ClassroomMovementApi {

    fun saveClassroomMovement(classroomId: UUID, request: DomainClassroomMovementRequest)
}
