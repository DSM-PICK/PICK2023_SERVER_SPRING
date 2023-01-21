package com.pickdsm.pickserverspring.domain.selfstudydirector.mapper

import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.SelfStudyDirectorEntity

interface SelfStudyDirectorMapper {

    fun domainToEntity(selfStudyDirector: SelfStudyDirector): SelfStudyDirectorEntity

    fun entityToDomain(selfStudyDirectorEntity: SelfStudyDirectorEntity): SelfStudyDirector
}
