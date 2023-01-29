package com.pickdsm.pickserverspring.domain.classroom.persistence

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ClassroomRepository : CrudRepository<ClassroomEntity, UUID> {
    fun findClassroomEntityById(classroomId: UUID): Classroom?

    fun findClassroomEntityByFloor(floor: Int): List<ClassroomElement>
}
