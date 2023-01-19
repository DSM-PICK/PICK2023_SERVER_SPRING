package com.pickdsm.pickserverspring.domain.application.mapper

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.persistence.entity.StatusEntity

interface StatusMapper {

    fun domainToEntity(status: Status): StatusEntity

    fun entityToDomain(statusEntity: StatusEntity): Status
}
