package com.pickdsm.pickserverspring.domain.classroom.persistence

import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomMovementEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ClassroomMovementRepository : CrudRepository<ClassroomMovementEntity, UUID>
