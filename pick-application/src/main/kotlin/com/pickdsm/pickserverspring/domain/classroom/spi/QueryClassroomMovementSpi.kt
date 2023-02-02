package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement

interface QueryClassroomMovementSpi {

    fun getAllClassroomMovement(): List<ClassroomMovement>
}
