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
            type = status.type,
            date = status.date,
            startTime = status.startTime,
            endTime = status.endTime,
        )
    }

    override fun entityToDomain(statusEntity: StatusEntity): Status {
        return Status(
            id = statusEntity.id,
            studentId = statusEntity.studentId,
            teacherId = statusEntity.teacherId,
            type = statusEntity.type,
            date = statusEntity.date,
            startTime = statusEntity.startTime,
            endTime = statusEntity.endTime,
        )
    }
}
