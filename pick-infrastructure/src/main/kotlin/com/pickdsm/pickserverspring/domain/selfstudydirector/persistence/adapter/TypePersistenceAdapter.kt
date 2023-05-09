package com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.adapter

import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.Type
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.TypeNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.mapper.TypeMapper
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.TypeRepository
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.QTypeEntity.typeEntity
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.TypeSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import java.util.UUID

@Adapter
class TypePersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val typeMapper: TypeMapper,
    private val typeRepository: TypeRepository,
) : TypeSpi {

    override fun queryTypeListByDate(startDate: LocalDate): List<Type> =
        jpaQueryFactory
            .selectFrom(typeEntity)
            .where(typeEntity.date.between(startDate, startDate.plusMonths(1)))
            .fetch()
            .map(typeMapper::entityToDomain)

    override fun queryTypeById(typeId: UUID): Type =
        typeRepository.findTypeEntityById(typeId)
            ?: throw TypeNotFoundException

    override fun queryTypeByToday(): Type? =
        jpaQueryFactory
            .selectFrom(typeEntity)
            .where(typeEntity.date.eq(LocalDate.now()))
            .fetchOne()
            ?.let(typeMapper::entityToDomain)

    override fun queryDirectorTypeByDate(date: LocalDate): DirectorType? =
        jpaQueryFactory
            .select(typeEntity.type)
            .from(typeEntity)
            .where(typeEntity.date.eq(date))
            .fetchOne()

    override fun queryTypeByDate(date: LocalDate): Type? =
        jpaQueryFactory
            .selectFrom(typeEntity)
            .where(typeEntity.date.eq(date))
            .fetchOne()
            ?.let(typeMapper::entityToDomain)

    override fun queryTypeIdByDate(date: LocalDate): UUID? =
        jpaQueryFactory
            .select(typeEntity.id)
            .from(typeEntity)
            .where(typeEntity.date.eq(date))
            .fetchOne()

    override fun saveType(type: Type) {
        val typeEntity = typeMapper.domainToEntity(type)
        typeRepository.save(typeEntity)
    }
}
