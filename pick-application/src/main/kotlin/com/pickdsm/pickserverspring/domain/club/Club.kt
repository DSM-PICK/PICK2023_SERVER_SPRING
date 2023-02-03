package com.pickdsm.pickserverspring.domain.club

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
class Club(

    val id: UUID = UUID.randomUUID(),

    val name: String,

    val headId: UUID,

    val teacherId: UUID,

    val classroomEntity: UUID,
)
