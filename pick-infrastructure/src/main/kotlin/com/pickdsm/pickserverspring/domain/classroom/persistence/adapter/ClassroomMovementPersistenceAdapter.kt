package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.mapper.StatusMapper
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.mapper.ClassroomMovementMapper
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomMovementRepository
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomMovementSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter

@Adapter
class ClassroomMovementPersistenceAdapter(
    private val classroomMovementRepository: ClassroomMovementRepository,
    private val statusMapper: StatusMapper,
    private val classroomMovementMapper: ClassroomMovementMapper,
) : ClassroomMovementSpi {

    override fun saveClassroomMovement(classroomMovement: ClassroomMovement) {
        classroomMovementRepository.save(
            classroomMovementMapper.domainToEntity(classroomMovement)
        )
    }

    override fun queryClassroomMovementByStatus(status: Status): ClassroomMovement {
        val statusEntity = statusMapper.domainToEntity(status)
        val classroomMovementEntity = classroomMovementRepository.findByStatusEntity(statusEntity)
        return classroomMovementMapper.entityToDomain(classroomMovementEntity)
    }
}
