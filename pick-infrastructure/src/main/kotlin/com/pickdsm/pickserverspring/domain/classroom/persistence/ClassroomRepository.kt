package com.pickdsm.pickserverspring.domain.classroom.persistence

import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ClassroomRepository: JpaRepository<ClassroomEntity, UUID>

