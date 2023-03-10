package com.pickdsm.pickserverspring.domain.application.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.mapper.ApplicationMapper
import com.pickdsm.pickserverspring.domain.application.persistence.ApplicationRepository
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QApplicationEntity.applicationEntity
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QStatusEntity.statusEntity
import com.pickdsm.pickserverspring.domain.application.spi.ApplicationSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate

@Adapter
class ApplicationPersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val applicationMapper: ApplicationMapper,
    private val applicationRepository: ApplicationRepository,
) : ApplicationSpi {

    override fun saveApplication(application: Application) {
        applicationRepository.save(applicationMapper.domainToEntity(application))
    }

    override fun queryPicnicApplicationListByToday(date: LocalDate): List<Application> =
        jpaQueryFactory
            .selectFrom(applicationEntity)
            .innerJoin(applicationEntity.statusEntity, statusEntity)
            .on(applicationEntity.statusEntity.date.eq(date))
            .fetch()
            .map(applicationMapper::entityToDomain)

    override fun queryApplicationListByToday(date: LocalDate): List<Application> =
        jpaQueryFactory
            .selectFrom(applicationEntity)
            .innerJoin(applicationEntity.statusEntity, statusEntity)
            .on(applicationEntity.statusEntity.date.eq(date))
            .fetch()
            .map(applicationMapper::entityToDomain)
}
