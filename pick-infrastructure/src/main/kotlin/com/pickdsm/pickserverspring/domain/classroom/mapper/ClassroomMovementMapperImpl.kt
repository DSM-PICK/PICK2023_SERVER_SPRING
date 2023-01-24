package com.pickdsm.pickserverspring.domain.classroom.mapper

import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomMovementEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ClassroomMovementMapperImpl(
    private val classroomRepository: ClassroomRepository,
) : ClassroomMovementMapper {

    override fun domainToEntity(classroomMovement: ClassroomMovement): ClassroomMovementEntity {
        val classroomEntity = classroomRepository.findByIdOrNull(classroomMovement.classroomId) ?: throw ClassroomNotFoundException

        return ClassroomMovementEntity(
            id = classroomMovement.id,
            studentId = classroomMovement.studentId,
            classroomEntity = classroomEntity,
        )
    }

    override fun entityToDomain(classroomMovementEntity: ClassroomMovementEntity): ClassroomMovement {
        return ClassroomMovement(
            id = classroomMovementEntity.id,
            studentId = classroomMovementEntity.studentId,
            classroomId = classroomMovementEntity.getClassroomId(),
        )
    }
}
