package com.pickdsm.pickserverspring.domain.classroom.api

import com.pickdsm.pickserverspring.domain.classroom.api.dto.request.DomainClassroomMovementRequest

interface ClassroomMovementApi {

    fun saveClassroomMovement(request: DomainClassroomMovementRequest)
}
