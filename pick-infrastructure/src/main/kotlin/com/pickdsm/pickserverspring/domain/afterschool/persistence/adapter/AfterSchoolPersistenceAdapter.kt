package com.pickdsm.pickserverspring.domain.afterschool.persistence.adapter

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.mapper.AfterSchoolMapper
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.QAfterSchoolEntity.afterSchoolEntity
import com.pickdsm.pickserverspring.domain.afterschool.spi.AfterSchoolSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory

@Adapter
class AfterSchoolPersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val afterSchoolMapper: AfterSchoolMapper,
) : AfterSchoolSpi {

    override fun queryAfterSchoolList(): List<AfterSchool> =
        jpaQueryFactory
            .selectFrom(afterSchoolEntity)
            .fetch()
            .map(afterSchoolMapper::entityToDomain)
}
