package com.pickdsm.pickserverspring.domain.application.persistence

import com.pickdsm.pickserverspring.domain.application.persistence.entity.StatusEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StatusRepository: JpaRepository<StatusEntity, UUID>