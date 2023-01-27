package com.pickdsm.pickserverspring.domain.application.persistence

import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QApplicationEntity.applicationEntity
import com.pickdsm.pickserverspring.domain.application.spi.ApplicationSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import java.util.UUID

@Adapter
class ApplicationPersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val applicationRepository: ApplicationRepository,
) : ApplicationSpi {

    override fun queryPicnicApplicationListByToday(date: LocalDate): List<Application> {
        return applicationRepository.findAllByDate(date)
    }

    override fun queryAllStudentId(): List<UUID> {
        return jpaQueryFactory
            .select(applicationEntity.studentId)
            .from(applicationEntity)
            .fetch()
    }
}
