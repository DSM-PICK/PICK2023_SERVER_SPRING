package com.pickdsm.pickserverspring.domain.application.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Status
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
    private val jpaQueryFactory: JPAQueryFactory,
) : StatusSpi {

    override fun saveAllStatus(statusList: List<Status>) {
        val statusEntityList = statusList.map {
            statusMapper.domainToEntity(it)
        }
        statusRepository.saveAll(statusEntityList)
    }
    override fun queryStatusList(data: LocalDate): List<QueryUserStatus> {
        TODO("Not yet implemented")
    }

    override fun queryPicnicStudentIdListByToday(date: LocalDate): List<UUID> {
        return jpaQueryFactory
            .select(statusEntity.studentId)
            .from(statusEntity)
            .where(statusEntity.date.eq(date))
            .fetch()
    }

    override fun queryPicnicStudentInfoListByToday(date: LocalDate): List<Status> {
        return jpaQueryFactory
            .selectFrom(statusEntity)
            .where(statusEntity.date.eq(date))
            .fetch()
            .map(statusMapper::entityToDomain)
    }
}
