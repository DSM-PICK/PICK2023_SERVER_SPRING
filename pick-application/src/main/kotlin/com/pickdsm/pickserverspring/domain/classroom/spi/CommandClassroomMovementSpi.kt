package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.classroom.Classroom

interface CommandClassroomMovementSpi {
    fun saveClassroomMovement(status: Status, classroom: Classroom)
}
