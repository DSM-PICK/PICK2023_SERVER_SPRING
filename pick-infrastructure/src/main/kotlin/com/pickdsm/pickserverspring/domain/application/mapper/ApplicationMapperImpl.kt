package com.pickdsm.pickserverspring.domain.application.mapper

import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.persistence.StatusRepository
import com.pickdsm.pickserverspring.domain.application.persistence.entity.ApplicationEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ApplicationMapperImpl(
    private val statusRepository: StatusRepository,
) : ApplicationMapper {

    override fun domainToEntity(application: Application): ApplicationEntity {
        val statusEntity = statusRepository.findByIdOrNull(application.id) ?: throw StatusNotFoundException

        return ApplicationEntity(
            id = statusEntity.id,
            statusEntity = statusEntity,
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
            date = applicationEntity.date,
            startTime = applicationEntity.startTime,
            endTime = applicationEntity.endTime,
            reason = applicationEntity.reason,
            isStatus = applicationEntity.isStatus,
            isPermission = applicationEntity.isPermission,
        )
    }
}
