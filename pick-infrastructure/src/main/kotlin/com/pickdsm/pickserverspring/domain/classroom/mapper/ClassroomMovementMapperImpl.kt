package com.pickdsm.pickserverspring.domain.classroom.mapper

import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.persistence.StatusRepository
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomMovementEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ClassroomMovementMapperImpl(
    private val statusRepository: StatusRepository,
    private val classroomRepository: ClassroomRepository,
) : ClassroomMovementMapper {

    override fun domainToEntity(classroomMovement: ClassroomMovement): ClassroomMovementEntity {
        val statusEntity = statusRepository.findByIdOrNull(classroomMovement.id) ?: throw StatusNotFoundException
        val classroomEntity = classroomRepository.findByIdOrNull(classroomMovement.classroomId) ?: throw ClassroomNotFoundException

        return ClassroomMovementEntity(
            id = statusEntity.id,
            statusEntity = statusEntity,
            classroomEntity = classroomEntity,
        )
    }

    override fun entityToDomain(classroomMovementEntity: ClassroomMovementEntity): ClassroomMovement {
        return ClassroomMovement(
            id = classroomMovementEntity.id,
            classroomId = classroomMovementEntity.getClassroomId(),
        )
    }
}
