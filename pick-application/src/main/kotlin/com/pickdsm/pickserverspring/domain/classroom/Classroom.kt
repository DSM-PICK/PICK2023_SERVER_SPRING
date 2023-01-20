package com.pickdsm.pickserverspring.domain.classroom

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
class Classroom(

    val id: UUID,

    val name: String,

    val floor: Int
)
