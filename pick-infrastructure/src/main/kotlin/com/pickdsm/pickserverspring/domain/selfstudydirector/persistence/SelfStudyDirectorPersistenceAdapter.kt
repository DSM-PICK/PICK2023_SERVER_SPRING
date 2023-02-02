package com.pickdsm.pickserverspring.domain.selfstudydirector.persistence

import com.pickdsm.pickserverspring.common.feign.client.UserClient
import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector
import com.pickdsm.pickserverspring.domain.selfstudydirector.mapper.SelfStudyDirectorMapper
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.QSelfStudyDirectorEntity.selfStudyDirectorEntity
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.SelfStudyDirectorSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import java.util.*

@Adapter
class SelfStudyDirectorPersistenceAdapter(
    private val selfStudyDirectorMapper: SelfStudyDirectorMapper,
    private val jpaQueryFactory: JPAQueryFactory,
    private val userClient: UserClient,
) : SelfStudyDirectorSpi {

    override fun querySelfStudyDirectorByDate(date: LocalDate): List<SelfStudyDirector> =
        jpaQueryFactory
            .select(selfStudyDirectorEntity)
            .from(selfStudyDirectorEntity)
            .where(selfStudyDirectorEntity.date.between(date, date.plusMonths(1)))
            .fetch()
            .map { selfStudyDirectorMapper.entityToDomain(it) }

    override fun querySelfStudyDirectorTeacherIdByDate(date: LocalDate): List<UUID> =
        jpaQueryFactory
            .select(selfStudyDirectorEntity.teacherId)
            .from(selfStudyDirectorEntity)
            .where(selfStudyDirectorEntity.date.between(date, date.plusMonths(1)))
            .fetch()

}
