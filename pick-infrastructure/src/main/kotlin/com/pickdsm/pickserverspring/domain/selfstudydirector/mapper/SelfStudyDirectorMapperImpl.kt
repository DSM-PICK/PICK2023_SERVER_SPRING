package com.pickdsm.pickserverspring.domain.selfstudydirector.mapper

import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.SelfStudyDirectorEntity
import org.springframework.stereotype.Component

@Component
class SelfStudyDirectorMapperImpl : SelfStudyDirectorMapper {

    override fun domainToEntity(selfStudyDirector: SelfStudyDirector): SelfStudyDirectorEntity {
        return SelfStudyDirectorEntity(
            id = selfStudyDirector.id,
            floor = selfStudyDirector.floor,
            teacherId = selfStudyDirector.teacherId,
            date = selfStudyDirector.date,
            type = selfStudyDirector.type,
        )
    }

    override fun entityToDomain(selfStudyDirectorEntity: SelfStudyDirectorEntity): SelfStudyDirector {
        return SelfStudyDirector(
            id = selfStudyDirectorEntity.id,
            floor = selfStudyDirectorEntity.floor,
            teacherId = selfStudyDirectorEntity.teacherId,
            date = selfStudyDirectorEntity.date,
            type = selfStudyDirectorEntity.type,
        )
    }
}
