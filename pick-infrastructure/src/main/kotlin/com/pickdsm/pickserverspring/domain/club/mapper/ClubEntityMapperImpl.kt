package com.pickdsm.pickserverspring.domain.club.mapper

import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.club.Club
import com.pickdsm.pickserverspring.domain.club.persistence.entity.ClubEntity
import org.springframework.stereotype.Component

@Component
class ClubEntityMapperImpl(
    private val classroomRepository: ClassroomRepository
) : ClubEntityMapper {

    override fun domainToEntity(club: Club): ClubEntity {
        val classroomEntity = classroomRepository.getReferenceById(club.id)

        return ClubEntity(
            id = club.id,
            name = club.name,
            classroomEntity = classroomEntity,
        )
    }

    override fun entityToDomain(clubEntity: ClubEntity): Club {
        return Club(
            id = clubEntity.id,
            name = clubEntity.name,
            classroomEntity = clubEntity.getClassroomId()
        )
    }
}
