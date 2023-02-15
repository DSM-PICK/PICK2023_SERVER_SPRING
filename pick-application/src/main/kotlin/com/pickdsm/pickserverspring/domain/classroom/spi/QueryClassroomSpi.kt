package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import java.util.*

interface QueryClassroomSpi {
    fun queryClassroomById(classroomId: UUID): Classroom

    fun queryClassroomListByFloor(floor: Int): List<Classroom>
}
