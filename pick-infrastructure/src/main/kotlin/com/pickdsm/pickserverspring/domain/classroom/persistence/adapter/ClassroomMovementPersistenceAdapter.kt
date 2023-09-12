package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QStatusEntity.statusEntity
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.mapper.ClassroomMovementMapper
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomMovementRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomEntity.classroomEntity
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
            .on(classroomMovementEntity.statusEntity.id.eq(status.id))
            .where(statusEntity.date.eq(LocalDate.now()))
            .fetchOne()
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

    override fun queryClassroomMovementClassroomIdByStatusId(statusId: UUID): UUID? =
        jpaQueryFactory
            .select(classroomMovementEntity.classroomEntity.id)
            .from(classroomMovementEntity)
            .where(classroomMovementEntity.statusEntity.id.eq(statusId))
            .fetchOne()

    override fun queryClassroomMovementListByClassroomId(classroomId: UUID): List<ClassroomMovement> =
        jpaQueryFactory
            .selectFrom(classroomMovementEntity)
            .join(classroomEntity)
            .on(classroomMovementEntity.classroomEntity.id.eq(classroomId))
            .join(statusEntity)
            .on(classroomMovementEntity.statusEntity.id.eq(statusEntity.id))
            .where(
                statusEntity.date.eq(LocalDate.now()),
                statusEntity.type.eq(StatusType.MOVEMENT),
            )
            .fetch()
            .map(classroomMovementMapper::entityToDomain)
}
