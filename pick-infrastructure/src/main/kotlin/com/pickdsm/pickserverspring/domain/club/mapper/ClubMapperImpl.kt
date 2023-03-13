package com.pickdsm.pickserverspring.domain.club.mapper

import com.pickdsm.pickserverspring.domain.club.Club
import com.pickdsm.pickserverspring.domain.club.exception.ClubNotFoundException
import com.pickdsm.pickserverspring.domain.club.persistence.ClubInfoRepository
import com.pickdsm.pickserverspring.domain.club.persistence.entity.ClubEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ClubMapperImpl(
    private val clubInfoRepository: ClubInfoRepository,
) : ClubMapper {

    override fun domainToEntity(club: Club): ClubEntity {
        val clubInfoEntity = clubInfoRepository.findByIdOrNull(club.clubInfoId)
            ?: throw ClubNotFoundException

        return ClubEntity(
            id = club.id,
            studentId = club.studentId,
            clubInfoEntity = clubInfoEntity
        )
    }

    override fun entityToDomain(clubEntity: ClubEntity): Club {
        return Club(
            id = clubEntity.id,
            studentId = clubEntity.studentId,
            clubInfoId = clubEntity.clubInfoEntity.id
        )
    }
}
