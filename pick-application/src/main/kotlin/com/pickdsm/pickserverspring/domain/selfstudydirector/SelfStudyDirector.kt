package com.pickdsm.pickserverspring.domain.selfstudydirector

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
class SelfStudyDirector(

    val id: UUID = UUID.randomUUID(),

    val floor: Int,

    val teacherId: UUID,

    val restrictionMovement: Boolean = false,

    val typeId: UUID,
) {

    fun changeSelfStudyDirector(teacherId: UUID) =
        SelfStudyDirector(
            id = id,
            floor = floor,
            teacherId = teacherId,
            restrictionMovement = restrictionMovement,
            typeId = typeId,
        )
}
