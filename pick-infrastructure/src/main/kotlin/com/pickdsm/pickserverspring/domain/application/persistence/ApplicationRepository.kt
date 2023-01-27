package com.pickdsm.pickserverspring.domain.application.persistence

import com.pickdsm.pickserverspring.domain.application.Application
import com.pickdsm.pickserverspring.domain.application.persistence.entity.ApplicationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
interface ApplicationRepository : CrudRepository<ApplicationEntity, UUID> {

    fun findAllByDate(date: LocalDate): List<Application>
}
