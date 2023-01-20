package com.pickdsm.pickserverspring.domain.classroom.mapper


import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomMovementEntity

interface ClassroomMovementMapper {

    fun domainToEntity(classroomMovement: ClassroomMovement): ClassroomMovementEntity

    fun entityToDomain(classroomMovementEntity: ClassroomMovementEntity): ClassroomMovement
}
