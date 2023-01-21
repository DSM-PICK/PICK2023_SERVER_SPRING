package com.pickdsm.pickserverspring.domain.club.mapper

import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.club.Club
import com.pickdsm.pickserverspring.domain.club.persistence.entity.ClubEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ClubEntityMapperImpl(
    private val classroomRepository: ClassroomRepository,
) : ClubEntityMapper {

    override fun domainToEntity(club: Club): ClubEntity {
        val classroomEntity = classroomRepository.findByIdOrNull(club.id) ?: throw ClassroomNotFoundException

        return ClubEntity(
            id = club.id,
            name = club.name,
            headId = club.headId,
            teacherId = club.teacherId,
            classroomEntity = classroomEntity,
        )
    }

    override fun entityToDomain(clubEntity: ClubEntity): Club {
        return Club(
            id = clubEntity.id,
            name = clubEntity.name,
            headId = clubEntity.headId,
            teacherId = clubEntity.teacherId,
            classroomEntity = clubEntity.getClassroomId(),
        )
    }
}
