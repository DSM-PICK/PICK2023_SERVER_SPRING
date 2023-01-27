package com.pickdsm.pickserverspring.domain.classroom

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.*

@Aggregate
class Classroom(

    val id: UUID = UUID(0, 0),

    val name: String,

    val floor: Int,
)
