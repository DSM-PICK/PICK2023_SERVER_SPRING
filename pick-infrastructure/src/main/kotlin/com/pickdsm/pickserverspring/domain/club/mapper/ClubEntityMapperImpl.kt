package com.pickdsm.pickserverspring.domain.club.mapper

import com.pickdsm.pickserverspring.domain.club.Club
import com.pickdsm.pickserverspring.domain.club.persistence.entity.ClubEntity
import org.springframework.stereotype.Component

@Component
class ClubEntityMapperImpl : ClubEntityMapper {

    override fun domainToEntity(club: Club): ClubEntity {
        return ClubEntity(
            id = club.id,
            name = club.name,
            headId = club.headId,
            teacherId = club.teacherId,
            classroomId = club.classroomId,
        )
    }

    override fun entityToDomain(clubEntity: ClubEntity): Club {
        return Club(
            id = clubEntity.id,
            name = clubEntity.name,
            headId = clubEntity.headId,
            teacherId = clubEntity.teacherId,
            classroomId = clubEntity.classroomId,
        )
    }
}
