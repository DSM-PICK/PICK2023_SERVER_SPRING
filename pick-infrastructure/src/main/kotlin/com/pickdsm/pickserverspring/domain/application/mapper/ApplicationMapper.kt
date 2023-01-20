package com.pickdsm.pickserverspring.domain.application.mapper

import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.persistence.entity.ApplicationEntity

interface ApplicationMapper {

    fun domainToEntity(application: Application): ApplicationEntity

    fun entityToDomain(applicationEntity: ApplicationEntity): Application
}
