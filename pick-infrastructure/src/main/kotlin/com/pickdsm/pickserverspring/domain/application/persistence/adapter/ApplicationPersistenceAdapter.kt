package com.pickdsm.pickserverspring.domain.application.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.mapper.ApplicationMapper
import com.pickdsm.pickserverspring.domain.application.persistence.ApplicationRepository
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QApplicationEntity
import com.pickdsm.pickserverspring.domain.application.spi.ApplicationSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import java.util.*

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
        return applicationRepository.findAllByDate(date)
    }

    override fun queryAllStudentId(): List<UUID> {
        return jpaQueryFactory
            .select(QApplicationEntity.applicationEntity.studentId)
            .from(QApplicationEntity.applicationEntity)
            .fetch()
    }
}
