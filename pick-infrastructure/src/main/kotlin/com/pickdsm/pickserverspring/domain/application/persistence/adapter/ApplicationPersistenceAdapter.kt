package com.pickdsm.pickserverspring.domain.application.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.mapper.ApplicationMapper
import com.pickdsm.pickserverspring.domain.application.persistence.ApplicationRepository
import com.pickdsm.pickserverspring.domain.application.persistence.StatusRepository
import com.pickdsm.pickserverspring.domain.application.persistence.entity.ApplicationEntity
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QApplicationEntity.applicationEntity
import com.pickdsm.pickserverspring.domain.application.spi.ApplicationSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate

@Adapter
class ApplicationPersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val statusRepository: StatusRepository,
    private val applicationMapper: ApplicationMapper,
    private val applicationRepository: ApplicationRepository,
) : ApplicationSpi {

    override fun saveApplication(application: Application) {
        val statusEntity = statusRepository.findByIdOrNull(application.statusId)
            ?: throw StatusNotFoundException

        applicationRepository.save(
            ApplicationEntity(
                statusId = statusEntity.id,
                statusEntity = statusEntity,
                desiredStartTime = application.desiredStartTime,
                desiredEndTime = application.desiredEndTime,
                reason = application.reason,
            ),
        )
    }

    override fun queryPicnicApplicationListByToday(date: LocalDate): List<Application> {
        return jpaQueryFactory
            .selectFrom(applicationEntity)
            .where(applicationEntity.statusEntity.date.eq(date))
            .fetch()
            .map(applicationMapper::entityToDomain)
    }

    override fun queryApplicationListByToday(date: LocalDate): List<Application> {
        return jpaQueryFactory
            .selectFrom(applicationEntity)
            .where(applicationEntity.statusEntity.date.eq(date))
            .fetch()
            .map(applicationMapper::entityToDomain)
    }
}
