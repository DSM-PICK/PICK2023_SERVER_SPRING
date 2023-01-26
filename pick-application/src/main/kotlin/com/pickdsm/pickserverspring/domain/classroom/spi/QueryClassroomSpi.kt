package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import java.util.UUID

interface QueryClassroomSpi {
    fun queryClassroomById(classroomId: UUID): Classroom

    fun queryClassroomListByFloor(floor: Int): List<ClassroomElement>
}