package com.pickdsm.pickserverspring.domain.application.persistence

import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.mapper.ApplicationMapper
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QApplicationEntity.applicationEntity
import com.pickdsm.pickserverspring.domain.application.spi.ApplicationSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.*

@Adapter
class ApplicationPersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val applicationRepository: ApplicationRepository,
    private val applicationMapper: ApplicationMapper,
) : ApplicationSpi {

    override fun queryPicnicApplicationList(): List<Application> {
        return applicationRepository.findAll()
            .map(applicationMapper::entityToDomain)
    }

    override fun queryAllStudentId(): List<UUID> {
        return jpaQueryFactory
            .select(applicationEntity.studentId)
            .from(applicationEntity)
            .fetch()
    }
}
