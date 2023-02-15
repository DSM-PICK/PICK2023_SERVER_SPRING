package com.pickdsm.pickserverspring.domain.selfstudydirector.persistence

import com.pickdsm.pickserverspring.domain.selfstudydirector.Type
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.TypeEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TypeRepository : CrudRepository<TypeEntity, UUID> {

    fun findTypeEntityById(typeId: UUID): Type?
}
