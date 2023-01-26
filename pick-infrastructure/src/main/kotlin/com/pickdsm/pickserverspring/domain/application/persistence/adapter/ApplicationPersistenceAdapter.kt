package com.pickdsm.pickserverspring.domain.application.persistence.adapter

import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.mapper.ApplicationMapper
import com.pickdsm.pickserverspring.domain.application.persistence.ApplicationRepository
import com.pickdsm.pickserverspring.domain.application.spi.ApplicationSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter

@Adapter
class ApplicationPersistenceAdapter(
    private val applicationMapper: ApplicationMapper,
    private val applicationRepository: ApplicationRepository,
) : ApplicationSpi {

    override fun saveApplication(application: Application) {
        applicationRepository.save(
            applicationMapper.domainToEntity(application)
        )
    }
}
