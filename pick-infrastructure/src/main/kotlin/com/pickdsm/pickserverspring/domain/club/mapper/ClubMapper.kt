package com.pickdsm.pickserverspring.domain.club.mapper

import com.pickdsm.pickserverspring.domain.club.Club
import com.pickdsm.pickserverspring.domain.club.persistence.entity.ClubEntity

interface ClubMapper {

    fun domainToEntity(club: Club): ClubEntity

    fun entityToDomain(clubEntity: ClubEntity): Club
}
