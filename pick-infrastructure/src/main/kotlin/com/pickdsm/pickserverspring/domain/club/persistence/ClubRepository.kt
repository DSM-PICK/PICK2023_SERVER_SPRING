package com.pickdsm.pickserverspring.domain.club.persistence

import com.pickdsm.pickserverspring.domain.club.persistence.entity.ClubEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ClubRepository: JpaRepository<ClubEntity, UUID>

