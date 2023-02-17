package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.ClassroomType
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import java.util.*

interface QueryClassroomSpi {
    fun queryClassroomById(classroomId: UUID): Classroom

    fun queryClassroomListByFloorAndByType(floor: Int, classroomType: String): List<ClassroomElement>
}
