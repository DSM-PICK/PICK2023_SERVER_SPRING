package com.pickdsm.pickserverspring.domain.club

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.*

@Aggregate
class Club (

    val id: UUID,

    val name: String,

    val classroomEntity: UUID
)
