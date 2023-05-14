package com.pickdsm.pickserverspring.domain.classroom

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class ClassroomMovement(

    val id: UUID = UUID.randomUUID(),

    val classroomId: UUID,

    val statusId: UUID,
) {
    fun changeClassroomId(classroomId: UUID): ClassroomMovement {
        return copy(classroomId = classroomId)
    }
}
