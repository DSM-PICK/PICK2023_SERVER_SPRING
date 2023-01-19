package com.pickdsm.pickserverspring.domain.classroom.persistence

import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ClassroomMovementRepository: JpaRepository<ClassroomMovement, UUID>
