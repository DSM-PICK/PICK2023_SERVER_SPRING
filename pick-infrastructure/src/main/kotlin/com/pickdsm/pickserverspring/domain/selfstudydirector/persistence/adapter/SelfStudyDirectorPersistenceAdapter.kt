package com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.adapter

import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.SelfStudyDirectorNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.mapper.SelfStudyDirectorMapper
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.SelfStudyDirectorRepository
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.QSelfStudyDirectorEntity.selfStudyDirectorEntity
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.QTypeEntity.typeEntity
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.SelfStudyDirectorSpi
import com.pickdsm.pickserverspring.common.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import java.util.UUID

@Adapter
class SelfStudyDirectorPersistenceAdapter(
    private val selfStudyDirectorRepository: SelfStudyDirectorRepository,
    private val selfStudyDirectorMapper: SelfStudyDirectorMapper,
    private val jpaQueryFactory: JPAQueryFactory,
) : SelfStudyDirectorSpi {
    override fun setRestrictionMovementTrue(selfStudyDirector: SelfStudyDirector) {
        val selfStudyDirectorEntity = selfStudyDirectorMapper.domainToEntity(selfStudyDirector)

        selfStudyDirectorEntity.setRestrictionMovementTrue()
    }

    override fun querySelfStudyDirectorByDate(date: LocalDate): List<SelfStudyDirector> =
        jpaQueryFactory
            .selectFrom(selfStudyDirectorEntity)
            .join(typeEntity)
            .on(selfStudyDirectorEntity.typeEntity.id.eq(typeEntity.id))
            .where(typeEntity.date.between(date, date.plusMonths(1)))
            .fetch()
            .map(selfStudyDirectorMapper::entityToDomain)

    override fun queryResponsibleFloorByTeacherIdAndToday(teacherId: UUID): Int? =
        jpaQueryFactory
            .select(selfStudyDirectorEntity.floor)
            .from(selfStudyDirectorEntity)
            .join(typeEntity)
            .on(typeEntity.id.eq(selfStudyDirectorEntity.typeEntity.id))
            .where(
                selfStudyDirectorEntity.teacherId.eq(teacherId),
                typeEntity.date.eq(LocalDate.now()),
            )
            .fetchOne()

    override fun queryResponsibleFloorByTeacherIdAndTypeId(teacherId: UUID, typeId: UUID): Int? =
        jpaQueryFactory
            .select(selfStudyDirectorEntity.floor)
            .from(selfStudyDirectorEntity)
            .innerJoin(selfStudyDirectorEntity.typeEntity, typeEntity)
            .where(
                selfStudyDirectorEntity.teacherId.eq(teacherId),
                typeEntity.id.eq(typeId),
            )
            .fetchOne()

    override fun queryAllSelfStudyDirectorByTeacherIdAndDate(teacherId: UUID, date: LocalDate): List<SelfStudyDirector> =
        jpaQueryFactory
            .selectFrom(selfStudyDirectorEntity)
            .innerJoin(selfStudyDirectorEntity.typeEntity, typeEntity)
            .on(selfStudyDirectorEntity.typeEntity.id.eq(typeEntity.id))
            .where(
                selfStudyDirectorEntity.teacherId.eq(teacherId),
                typeEntity.date.eq(date),
            )
            .orderBy(selfStudyDirectorEntity.floor.asc())
            .fetch()
            .map(selfStudyDirectorMapper::entityToDomain)

    override fun querySelfStudyDirectorByTeacherId(teacherId: UUID): SelfStudyDirector {
        val selfStudyDirectorEntity = selfStudyDirectorRepository.findSelfStudyDirectorEntityByTeacherId(teacherId)
            ?: throw SelfStudyDirectorNotFoundException

        return selfStudyDirectorMapper.entityToDomain(selfStudyDirectorEntity)
    }

    override fun querySelfStudyDirectorByDateAndFloor(date: LocalDate, floor: Int): SelfStudyDirector? {
        val selfStudyDirectorEntity = jpaQueryFactory
            .selectFrom(selfStudyDirectorEntity)
            .innerJoin(selfStudyDirectorEntity.typeEntity, typeEntity)
            .on(selfStudyDirectorEntity.typeEntity.id.eq(typeEntity.id))
            .where(
                typeEntity.date.eq(date),
                selfStudyDirectorEntity.floor.eq(floor),
            )
            .fetchOne()
        return selfStudyDirectorEntity?.let(selfStudyDirectorMapper::entityToDomain)
    }

    override fun querySelfStudyDirectorByToday(): List<SelfStudyDirector> =
        jpaQueryFactory
            .selectFrom(selfStudyDirectorEntity)
            .join(typeEntity)
            .on(selfStudyDirectorEntity.typeEntity.id.eq(typeEntity.id))
            .where(typeEntity.date.eq(LocalDate.now()))
            .fetch()
            .map(selfStudyDirectorMapper::entityToDomain)

    override fun saveSelfStudyDirector(selfStudyDirector: SelfStudyDirector) {
        selfStudyDirectorRepository.save(selfStudyDirectorMapper.domainToEntity(selfStudyDirector))
    }

    override fun deleteSelfStudyDirector(selfStudyDirector: SelfStudyDirector) {
        selfStudyDirectorRepository.delete(selfStudyDirectorMapper.domainToEntity(selfStudyDirector))
    }
}
