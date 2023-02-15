package com.pickdsm.pickserverspring.domain.application.mapper

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.persistence.entity.StatusEntity
import org.springframework.stereotype.Component

@Component
class StatusMapperImpl : StatusMapper {

    override fun domainToEntity(status: Status): StatusEntity {
        return StatusEntity(
            id = status.id,
            studentId = status.studentId,
            teacherId = status.teacherId,
            date = status.date,
            startPeriod = status.startPeriod,
            endPeriod = status.endPeriod,
            type = status.type,
        )
    }

    override fun entityToDomain(statusEntity: StatusEntity): Status {
        return Status(
            id = statusEntity.id,
            studentId = statusEntity.studentId,
            teacherId = statusEntity.teacherId,
            date = statusEntity.date,
            startPeriod = statusEntity.startPeriod,
            endPeriod = statusEntity.endPeriod,
            type = statusEntity.type,
        )
    }
}
