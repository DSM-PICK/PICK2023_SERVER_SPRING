package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.mapper.ClassroomMovementMapper
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomMovementRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomMovementEntity.classroomMovementEntity
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomMovementSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory

@Adapter
class ClassroomMovementPersistenceAdapter(
    private val classroomMovementRepository: ClassroomMovementRepository,
    private val classroomMovementMapper: ClassroomMovementMapper,
    private val jpaQueryFactory: JPAQueryFactory,
) : ClassroomMovementSpi {

    override fun saveClassroomMovement(classroomMovement: ClassroomMovement) {
        classroomMovementRepository.save(
            classroomMovementMapper.domainToEntity(classroomMovement),
        )
    }

    override fun deleteClassroomMovement(classroomMovement: ClassroomMovement) {
        classroomMovementRepository.delete(
            classroomMovementMapper.domainToEntity(classroomMovement),
        )
    }

    override fun queryClassroomMovementByStatus(status: Status): ClassroomMovement? =
        jpaQueryFactory
            .selectFrom(classroomMovementEntity)
            .where(classroomMovementEntity.statusEntity.id.eq(status.id))
            .fetchFirst()
            ?.let(classroomMovementMapper::entityToDomain)
}
