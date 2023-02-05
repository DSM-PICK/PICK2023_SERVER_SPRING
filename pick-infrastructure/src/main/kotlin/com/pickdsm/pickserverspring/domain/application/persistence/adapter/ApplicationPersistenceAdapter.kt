package com.pickdsm.pickserverspring.domain.application.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.mapper.ApplicationMapper
import com.pickdsm.pickserverspring.domain.application.persistence.ApplicationRepository
import com.pickdsm.pickserverspring.domain.application.persistence.entity.ApplicationEntity
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QApplicationEntity.applicationEntity
import com.pickdsm.pickserverspring.domain.application.spi.ApplicationSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.jpa.JPAExpressions.selectFrom
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.time.LocalDate
import java.util.UUID

@Adapter
class ApplicationPersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val applicationMapper: ApplicationMapper,
    private val applicationRepository: ApplicationRepository,
) : ApplicationSpi {

    override fun saveApplication(application: Application) {
        applicationRepository.save(applicationMapper.domainToEntity(application))
    }

    override fun queryPicnicApplicationListByToday(date: LocalDate): List<Application> {
        return jpaQueryFactory
            .selectFrom(applicationEntity)
            .where(applicationEntity.date.eq(date))
            .fetch()
            .map(applicationMapper::entityToDomain)
    }

    override fun changePermission(applicationIdList: List<UUID>) {
        applicationRepository.findAllById(applicationIdList)
            .map(ApplicationEntity::changePermission)
    }

    override fun changeStatus(applicationId: UUID) {
        applicationRepository.findById(applicationId)
            .map(ApplicationEntity::changeStatus)
    }

    override fun deleteApplication(applicationId: UUID) {
        applicationRepository.deleteById(applicationId)
    }

    override fun queryApplicationListByToday(date: LocalDate): List<Application> {
        return jpaQueryFactory
            .selectFrom(applicationEntity)
            .where(applicationEntity.date.eq(date))
            .fetch()
            .map(applicationMapper::entityToDomain)
    }
}

