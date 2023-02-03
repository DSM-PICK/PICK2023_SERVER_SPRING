package com.pickdsm.pickserverspring.domain.classroom.api

import java.util.UUID

interface ClassroomMovementApi {

    fun saveClassroomMovement(classroomId: UUID)
}
