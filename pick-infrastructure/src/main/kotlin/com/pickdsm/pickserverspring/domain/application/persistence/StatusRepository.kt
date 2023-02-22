package com.pickdsm.pickserverspring.domain.application.persistence

import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.persistence.entity.StatusEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
interface StatusRepository : CrudRepository<StatusEntity, UUID> {

    fun findAllByTypeOrTypeAndDate(type: StatusType, type2: StatusType, date: LocalDate): List<StatusEntity>
}
