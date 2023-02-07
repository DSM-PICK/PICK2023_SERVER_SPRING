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
        val statusEntity = statusRepository.findByIdOrNull(application.statusId)
            ?: throw StatusNotFoundException

        return ApplicationEntity(
            statusId = application.statusId,
            statusEntity = statusEntity,
            desiredStartTime = application.desiredStartTime,
            desiredEndTime = application.desiredEndTime,
            reason = application.reason,
        )
    }

    override fun entityToDomain(applicationEntity: ApplicationEntity): Application {
        return Application(
            statusId = applicationEntity.statusId,
            desiredStartTime = applicationEntity.desiredStartTime,
            desiredEndTime = applicationEntity.desiredEndTime,
            reason = applicationEntity.reason,
        )
    }
}
