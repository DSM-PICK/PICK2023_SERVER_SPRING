package com.pickdsm.pickserverspring.domain.selfstudydirector.mapper

import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.TypeNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.TypeRepository
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.SelfStudyDirectorEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class SelfStudyDirectorMapperImpl(
    private val typeRepository: TypeRepository,
) : SelfStudyDirectorMapper {

    override fun domainToEntity(selfStudyDirector: SelfStudyDirector): SelfStudyDirectorEntity {
        val typeEntity = typeRepository.findByIdOrNull(selfStudyDirector.typeId)
            ?: throw TypeNotFoundException

        return SelfStudyDirectorEntity(
            id = selfStudyDirector.id,
            floor = selfStudyDirector.floor,
            teacherId = selfStudyDirector.teacherId,
            typeEntity = typeEntity,
            restrictionMovement = selfStudyDirector.restrictionMovement,
        )
    }

    override fun entityToDomain(selfStudyDirectorEntity: SelfStudyDirectorEntity): SelfStudyDirector {
        return SelfStudyDirector(
            id = selfStudyDirectorEntity.id,
            floor = selfStudyDirectorEntity.floor,
            teacherId = selfStudyDirectorEntity.teacherId,
            typeId = selfStudyDirectorEntity.typeEntity.id,
            restrictionMovement = selfStudyDirectorEntity.restrictionMovement,
        )
    }
}
