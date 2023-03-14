package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement

interface CommandClassroomMovementSpi {
    fun saveClassroomMovement(classroomMovement: ClassroomMovement)

    fun deleteClassroomMovement(classroomMovement: ClassroomMovement)
}
