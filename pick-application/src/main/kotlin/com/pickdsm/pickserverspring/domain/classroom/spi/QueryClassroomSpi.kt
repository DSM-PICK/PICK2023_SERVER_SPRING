package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import java.util.*

interface QueryClassroomSpi {
    fun queryClassroomById(classroomId: UUID): Classroom

    fun queryAllClassroomListByFloor(floor: Int): List<ClassroomElement>

    fun querySelfStudyClassroomListByFloor(floor: Int): List<ClassroomElement>
}
