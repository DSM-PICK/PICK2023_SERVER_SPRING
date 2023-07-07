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

    override fun queryPicnicApplicationListByToday(): List<Application> =
        jpaQueryFactory
            .selectFrom(applicationEntity)
            .innerJoin(applicationEntity.statusEntity, statusEntity)
            .on(applicationEntity.statusEntity.date.eq(LocalDate.now()))
            .fetch()
            .map(applicationMapper::entityToDomain)

    override fun queryApplicationByStudentIdAndStatusId(studentId: UUID, statusId: UUID): Application? =
        jpaQueryFactory
            .selectFrom(applicationEntity)
            .innerJoin(applicationEntity.statusEntity, statusEntity)
            .on(applicationEntity.statusEntity.id.eq(statusId))
            .where(
                statusEntity.studentId.eq(studentId),
                applicationEntity.isReturn.eq(false),
                )
            .fetchFirst()
            ?.let(applicationMapper::entityToDomain)
}
