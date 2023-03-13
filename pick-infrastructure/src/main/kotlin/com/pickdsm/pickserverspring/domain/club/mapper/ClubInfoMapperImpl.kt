package com.pickdsm.pickserverspring.domain.club.mapper

import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.club.ClubInfo
import com.pickdsm.pickserverspring.domain.club.persistence.entity.ClubInfoEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ClubInfoMapperImpl(
    private val classroomRepository: ClassroomRepository,
) : ClubInfoMapper {
    override fun domainToEntity(clubInfo: ClubInfo): ClubInfoEntity {
        val classroomEntity = classroomRepository.findByIdOrNull(clubInfo.classroomId)
            ?: throw ClassroomNotFoundException

        return ClubInfoEntity(
            id = clubInfo.id,
            name = clubInfo.name,
            teacherId = clubInfo.teacherId,
            headId = clubInfo.headId,
            classroomEntity = classroomEntity,
        )
    }

    override fun entityToDomain(clubInfoEntity: ClubInfoEntity): ClubInfo {
        return ClubInfo(
            id = clubInfoEntity.id,
            name = clubInfoEntity.name,
            teacherId = clubInfoEntity.teacherId,
            headId = clubInfoEntity.headId,
            classroomId = clubInfoEntity.classroomEntity.id,
        )
    }
}
