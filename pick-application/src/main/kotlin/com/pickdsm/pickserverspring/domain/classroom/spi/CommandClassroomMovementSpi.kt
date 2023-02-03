package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import java.util.UUID

interface CommandClassroomMovementSpi {
    fun saveClassroom(studentId: UUID, classroom: Classroom)
}
