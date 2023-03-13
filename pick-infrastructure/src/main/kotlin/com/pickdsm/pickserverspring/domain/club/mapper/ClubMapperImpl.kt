package com.pickdsm.pickserverspring.domain.club.mapper

import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.club.Club
import com.pickdsm.pickserverspring.domain.club.persistence.entity.ClubEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ClubMapperImpl(
    private val classroomRepository: ClassroomRepository,
) : ClubMapper {

    override fun domainToEntity(club: Club): ClubEntity {
        val classroomEntity = classroomRepository.findByIdOrNull(club.classroomId)
            ?: throw ClassroomNotFoundException

        return ClubEntity(
            id = club.id,
            clubId = club.clubId,
            name = club.name,
            teacherId = club.teacherId,
            studentId = club.studentId,
            classroomEntity = classroomEntity,
            headId = club.headId,
        )
    }

    override fun entityToDomain(clubEntity: ClubEntity): Club {
        return Club(
            id = clubEntity.id,
            clubId = clubEntity.clubId,
            name = clubEntity.name,
            teacherId = clubEntity.teacherId,
            studentId = clubEntity.studentId,
            classroomId = clubEntity.classroomEntity.id,
            headId = clubEntity.headId,
        )
    }
}
