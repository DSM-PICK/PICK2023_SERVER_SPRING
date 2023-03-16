package com.pickdsm.pickserverspring.domain.application.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.mapper.StatusMapper
import com.pickdsm.pickserverspring.domain.application.persistence.StatusRepository
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QStatusEntity.statusEntity
import com.pickdsm.pickserverspring.domain.application.spi.StatusSpi
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomEntity.classroomEntity
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomMovementEntity.classroomMovementEntity
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import java.util.UUID

@Adapter
class StatusPersistenceAdapter(
    private val statusMapper: StatusMapper,
    private val statusRepository: StatusRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : StatusSpi {

    override fun saveAllStatus(statusList: List<Status>) {
        val statusEntityList = statusList.map(statusMapper::domainToEntity)
        statusRepository.saveAll(statusEntityList)
    }

    override fun saveStatus(status: Status) {
        val statusEntity = statusMapper.domainToEntity(status)
        statusRepository.save(statusEntity)
    }

    override fun saveStatusAndGetStatusId(status: Status): UUID {
        val statusEntity = statusMapper.domainToEntity(status)
        val saveStatus = statusRepository.save(statusEntity)
        return saveStatus.id
    }

    override fun deleteAllMovementStudent(statusList: List<Status>) {
        val statusEntityList = statusList.map(statusMapper::domainToEntity)
        statusRepository.deleteAll(statusEntityList)
    }

    override fun deleteStatus(status: Status) {
        statusRepository.delete(
            statusMapper.domainToEntity(status),
        )
    }

    override fun queryPicnicStudentInfoListByToday(date: LocalDate): List<Status> =
        jpaQueryFactory
            .selectFrom(statusEntity)
            .where(statusEntity.date.eq(date), (statusEntity.type.eq(StatusType.PICNIC)))
            .fetch()
            .map(statusMapper::entityToDomain)

    override fun queryMovementStudentInfoListByToday(date: LocalDate): List<Status> =
        jpaQueryFactory
            .selectFrom(statusEntity)
            .where(statusEntity.date.eq(date), (statusEntity.type.eq(StatusType.MOVEMENT)))
            .fetch()
            .map(statusMapper::entityToDomain)

    override fun queryAwaitStudentListByToday(date: LocalDate): List<Status> =
        jpaQueryFactory
            .selectFrom(statusEntity)
            .where(statusEntity.date.eq(date), statusEntity.type.eq(StatusType.AWAIT))
            .fetch()
            .map(statusMapper::entityToDomain)

    override fun queryStatusListByToday(): List<Status> =
        jpaQueryFactory
            .selectFrom(statusEntity)
            .where(statusEntity.date.eq(LocalDate.now()))
            .fetch()
            .map(statusMapper::entityToDomain)

    override fun queryStatusListByDate(date: LocalDate): List<Status> =
        jpaQueryFactory
            .selectFrom(statusEntity)
            .where(statusEntity.date.eq(date))
            .fetch()
            .map(statusMapper::entityToDomain)

    override fun queryPicnicStudentByStudentId(studentId: UUID): Status? =
        jpaQueryFactory
            .selectFrom(statusEntity)
            .where(
                statusEntity.studentId.eq(studentId),
                statusEntity.type.eq(StatusType.PICNIC),
            )
            .fetchFirst()
            ?.let(statusMapper::entityToDomain)

    override fun queryStatusByStudentIdAndStartPeriodAndEndPeriod(
        studentId: UUID,
        startPeriod: Int,
        endPeriod: Int,
    ): Status? =
        jpaQueryFactory
            .selectFrom(statusEntity)
            .where(
                statusEntity.studentId.eq(studentId),
                statusEntity.startPeriod.eq(startPeriod),
                statusEntity.endPeriod.eq(endPeriod)
            )
            .fetchOne()
            ?.let(statusMapper::entityToDomain)

    override fun queryMovementStudentByStudentId(studentId: UUID): Status? =
        jpaQueryFactory
            .selectFrom(statusEntity)
            .where(statusEntity.studentId.eq(studentId), statusEntity.type.eq(StatusType.MOVEMENT))
            .fetchOne()
            ?.let(statusMapper::entityToDomain)

    override fun queryStatusByStudentId(studentId: UUID): Status? =
        jpaQueryFactory
            .selectFrom(statusEntity)
            .where(statusEntity.studentId.eq(studentId))
            .fetchFirst()
            ?.let(statusMapper::entityToDomain)

    override fun queryMovementStatusListByTodayAndClassroomId(classroomId: UUID): List<Status> =
        jpaQueryFactory
            .selectFrom(statusEntity)
            .distinct()
            .join(classroomMovementEntity)
            .on(statusEntity.id.eq(classroomMovementEntity.statusEntity.id))
            .join(classroomEntity)
            .on(classroomMovementEntity.classroomEntity.id.eq(classroomId))
            .where(statusEntity.date.eq(LocalDate.now()))
            .fetch()
            .map(statusMapper::entityToDomain)
}
