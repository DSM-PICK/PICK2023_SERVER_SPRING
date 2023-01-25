package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import java.util.UUID

interface QueryClassroomSpi {
    fun queryClassroomById(classroomId: UUID): Classroom
}