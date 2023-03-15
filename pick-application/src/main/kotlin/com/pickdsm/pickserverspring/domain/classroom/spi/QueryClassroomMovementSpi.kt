package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement

interface QueryClassroomMovementSpi {
    fun queryClassroomMovementByStatus(status: Status): ClassroomMovement?
}
