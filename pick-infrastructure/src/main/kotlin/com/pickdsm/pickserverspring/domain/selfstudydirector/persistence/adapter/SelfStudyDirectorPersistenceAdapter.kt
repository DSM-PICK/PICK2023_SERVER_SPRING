package com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.adapter

import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector
import com.pickdsm.pickserverspring.domain.selfstudydirector.mapper.SelfStudyDirectorMapper
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.QSelfStudyDirectorEntity.selfStudyDirectorEntity
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.QTypeEntity.typeEntity
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.SelfStudyDirectorSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import java.util.*

@Adapter
class SelfStudyDirectorPersistenceAdapter(
    private val selfStudyDirectorMapper: SelfStudyDirectorMapper,
    private val jpaQueryFactory: JPAQueryFactory,
) : SelfStudyDirectorSpi {

    override fun querySelfStudyDirectorByDate(date: LocalDate): List<SelfStudyDirector> =
        jpaQueryFactory
            .selectFrom(selfStudyDirectorEntity)
            .innerJoin(selfStudyDirectorEntity.typeEntity, typeEntity)
            .on(selfStudyDirectorEntity.typeEntity.id.eq(typeEntity.id))
            .where(typeEntity.date.between(date, date.plusMonths(1)))
            .fetch()
            .map(selfStudyDirectorMapper::entityToDomain)

    override fun queryResponsibleFloorByTeacherId(teacherId: UUID): Int? =
        jpaQueryFactory
            .select(selfStudyDirectorEntity.floor)
            .from(selfStudyDirectorEntity)
            .where(selfStudyDirectorEntity.teacherId.eq(teacherId))
            .fetchOne()
}