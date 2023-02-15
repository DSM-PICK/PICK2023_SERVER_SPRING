package com.pickdsm.pickserverspring.domain.club.persistence.adapter

import com.pickdsm.pickserverspring.domain.club.Club
import com.pickdsm.pickserverspring.domain.club.mapper.ClubMapper
import com.pickdsm.pickserverspring.domain.club.persistence.entity.QClubEntity.clubEntity
import com.pickdsm.pickserverspring.domain.club.spi.ClubSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory

@Adapter
class ClubPersistenceAdapter(
    private val clubMapper: ClubMapper,
    private val jpaQueryFactory: JPAQueryFactory,
) : ClubSpi {

    override fun queryClubList(): List<Club> =
        jpaQueryFactory
            .selectFrom(clubEntity)
            .fetch()
            .map(clubMapper::entityToDomain)
}
