package com.pickdsm.pickserverspring.domain.classroom.mapper

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomEntity

interface ClassroomMapper {

    fun domainToEntity(classroom: Classroom): ClassroomEntity

    fun entityToDomain(classroomEntity: ClassroomEntity): Classroom
}