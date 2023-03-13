package com.pickdsm.pickserverspring.domain.club.mapper

import com.pickdsm.pickserverspring.domain.club.ClubInfo
import com.pickdsm.pickserverspring.domain.club.persistence.entity.ClubInfoEntity

interface ClubInfoMapper {

    fun domainToEntity(clubInfo: ClubInfo): ClubInfoEntity

    fun entityToDomain(clubInfoEntity: ClubInfoEntity): ClubInfo
}
