package com.pickdsm.pickserverspring.domain.application.mapper

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.persistence.entity.StatusEntity
import org.springframework.stereotype.Component

@Component
class StatusMapperImpl : StatusMapper {

    override fun domainToEntity(status: Status): StatusEntity {
        return StatusEntity(
            id = status.id,
            type = status.type,
            date = status.date,
        )
    }

    override fun entityToDomain(statusEntity: StatusEntity): Status {
        return Status(
            id = statusEntity.id,
            type = statusEntity.type,
            date = statusEntity.date,
        )
    }
}
