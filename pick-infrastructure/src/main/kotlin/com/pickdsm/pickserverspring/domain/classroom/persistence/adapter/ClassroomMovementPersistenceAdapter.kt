package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QStatusEntity.statusEntity
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.mapper.ClassroomMovementMapper
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomMovementRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomMovementEntity.classroomMovementEntity
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomMovementSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import java.util.UUID

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
            .join(statusEntity)
            .on(classroomMovementEntity.statusEntity.id.eq(statusEntity.id))
            .fetchFirst()
            ?.let(classroomMovementMapper::entityToDomain)

    override fun existClassroomMovementByStudentId(studentId: UUID): Boolean =
        jpaQueryFactory
            .select(classroomMovementEntity.id)
            .from(classroomMovementEntity)
            .join(statusEntity)
            .on(classroomMovementEntity.statusEntity.id.eq(statusEntity.id))
            .where(
                statusEntity.studentId.eq(studentId),
                statusEntity.date.eq(LocalDate.now()),
            )
            .fetchOne() != null

    override fun queryClassroomMovementByStudentIdAndToday(studentId: UUID): ClassroomMovement? =
        jpaQueryFactory
            .selectFrom(classroomMovementEntity)
            .join(statusEntity)
            .on(classroomMovementEntity.statusEntity.id.eq(statusEntity.id))
            .where(
                statusEntity.studentId.eq(studentId),
                statusEntity.date.eq(LocalDate.now()),
            )
            .fetchOne()
            ?.let(classroomMovementMapper::entityToDomain)
}
