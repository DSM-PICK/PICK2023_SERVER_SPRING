package com.pickdsm.pickserverspring.domain.application.mapper

import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.persistence.entity.ApplicationEntity
import org.springframework.stereotype.Component

@Component
class ApplicationMapperImpl : ApplicationMapper {

    override fun domainToEntity(application: Application): ApplicationEntity {
        return ApplicationEntity(
            id = application.id,
            studentId = application.studentId,
            date = application.date,
            startTime = application.startTime,
            endTime = application.endTime,
            reason = application.reason,
            isStatus = application.isStatus,
            isPermission = application.isPermission,
        )
    }

    override fun entityToDomain(applicationEntity: ApplicationEntity): Application {
        return Application(
            id = applicationEntity.id,
            studentId = applicationEntity.studentId,
            date = applicationEntity.date,
            startTime = applicationEntity.startTime,
            endTime = applicationEntity.endTime,
            reason = applicationEntity.reason,
            isStatus = applicationEntity.isStatus,
            isPermission = applicationEntity.isPermission,
        )
    }
}
