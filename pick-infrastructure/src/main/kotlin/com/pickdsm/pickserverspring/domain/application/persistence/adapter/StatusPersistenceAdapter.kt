package com.pickdsm.pickserverspring.domain.application.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.mapper.StatusMapper
import com.pickdsm.pickserverspring.domain.application.persistence.StatusRepository
import com.pickdsm.pickserverspring.domain.application.persistence.entity.QStatusEntity.statusEntity
import com.pickdsm.pickserverspring.domain.application.persistence.entity.StatusEntity
import com.pickdsm.pickserverspring.domain.application.spi.StatusSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.UUID

@Adapter
class StatusPersistenceAdapter(
    private val statusMapper: StatusMapper,
    private val statusRepository: StatusRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : StatusSpi {

    override fun saveAllStatus(statusList: List<Status>) {
        val statusEntityList = statusList.map {
            statusMapper.domainToEntity(it)
        }
        statusRepository.saveAll(statusEntityList)
    }

    override fun changeStatusToPicnic(statusIdList: List<UUID>) {
        val statusList = statusRepository.findAllById(statusIdList)
            .map(StatusEntity::changeStatusToPicnic)

        statusRepository.saveAll(statusList)
    }

    override fun queryStatusIdList(): List<UUID> {
        return jpaQueryFactory
            .select(statusEntity.id)
            .from(statusEntity)
            .fetch()
    }
}
