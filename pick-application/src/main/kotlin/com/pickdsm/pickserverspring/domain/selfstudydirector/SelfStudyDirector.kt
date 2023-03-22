package com.pickdsm.pickserverspring.domain.selfstudydirector

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class SelfStudyDirector(

    val id: UUID = UUID.randomUUID(),

    val floor: Int,

    val teacherId: UUID,

    val restrictionMovement: Boolean = false,

    val typeId: UUID,
) {
    fun changeSelfStudyDirector(teacherId: UUID): SelfStudyDirector {
        return copy(
            teacherId = teacherId,
        )
    }

    fun setBlockClassroomMovementFalse(): SelfStudyDirector {
        return copy(
            restrictionMovement = false
        )
    }
}
