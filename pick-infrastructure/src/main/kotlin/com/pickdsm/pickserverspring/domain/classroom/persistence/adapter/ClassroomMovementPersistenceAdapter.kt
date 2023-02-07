package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.mapper.StatusMapper
import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.mapper.ClassroomMapper
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomMovementRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomMovementEntity
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomMovementSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter

@Adapter
class ClassroomMovementPersistenceAdapter(
    private val classroomMovementRepository: ClassroomMovementRepository,
    private val classroomMapper: ClassroomMapper,
    private val statusMapper: StatusMapper,
) : ClassroomMovementSpi {

    override fun saveClassroom(status: Status, classroom: Classroom) {
        val statusEntity = statusMapper.domainToEntity(status)
        val classroomEntity = classroomMapper.domainToEntity(classroom)

        classroomMovementRepository.save(
            ClassroomMovementEntity(
                statusId = statusEntity.id,
                statusEntity = statusEntity,
                classroomEntity = classroomEntity,
            ),
        )
    }
}
