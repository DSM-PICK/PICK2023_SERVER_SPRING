package com.pickdsm.pickserverspring.domain.application.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryUserStatus
import com.pickdsm.pickserverspring.domain.application.mapper.StatusMapper
import com.pickdsm.pickserverspring.domain.application.persistence.StatusRepository
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QStatusEntity.statusEntity
import com.pickdsm.pickserverspring.domain.application.spi.StatusSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import java.util.*

@Adapter
class StatusPersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val statusMapper: StatusMapper,
    private val statusRepository: StatusRepository,
) : StatusSpi {

    override fun saveAllStatus(statusList: List<Status>) {
        val statusEntityList = statusList.map {
            statusMapper.domainToEntity(it)
        }
        statusRepository.saveAll(statusEntityList)
    }

    override fun saveStatus(status: Status) {
        val statusEntity = statusMapper.domainToEntity(status)
        statusRepository.save(statusEntity)
    }

    override fun queryPicnicStudentInfoListByToday(date: LocalDate): List<Status> {
        return jpaQueryFactory
            .selectFrom(statusEntity)
            .where(statusEntity.date.eq(date), (statusEntity.type.eq(StatusType.PICNIC)))
            .fetch()
            .map(statusMapper::entityToDomain)
    }

    override fun queryMovementStudentInfoListByToday(date: LocalDate): List<Status> {
        return jpaQueryFactory
            .selectFrom(statusEntity)
            .where(statusEntity.date.eq(date), (statusEntity.type.eq(StatusType.MOVEMENT)))
            .fetch()
            .map(statusMapper::entityToDomain)
    }
}
