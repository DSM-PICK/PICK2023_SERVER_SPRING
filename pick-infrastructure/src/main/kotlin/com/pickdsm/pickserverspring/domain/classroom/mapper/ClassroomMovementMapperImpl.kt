package com.pickdsm.pickserverspring.domain.classroom.mapper

import com.pickdsm.pickserverspring.domain.application.persistence.StatusRepository
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomMovementEntity
import org.springframework.stereotype.Component

@Component
class ClassroomMovementMapperImpl(
    private val statusRepository: StatusRepository,
    private val classroomRepository: ClassroomRepository
) : ClassroomMovementMapper {

    override fun domainToEntity(classroomMovement: ClassroomMovement): ClassroomMovementEntity {
        val statusEntity = statusRepository.getReferenceById(classroomMovement.statusEntity)
        val classroomEntity = classroomRepository.getReferenceById(classroomMovement.classroomEntity)

        return ClassroomMovementEntity(
            statusEntity = statusEntity,
            classroomEntity = classroomEntity
        )
    }

    override fun entityToDomain(classroomMovementEntity: ClassroomMovementEntity): ClassroomMovement {
        return ClassroomMovement(

            statusEntity = classroomMovementEntity.getStatusId(),
            classroomEntity = classroomMovementEntity.getClassroomId()
        )
    }
}
