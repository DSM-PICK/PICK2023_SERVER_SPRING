package com.pickdsm.pickserverspring.domain.classroom

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Classroom(

    val id: UUID = UUID.randomUUID(),

    val name: String,

    val floor: Int,

    val grade: Int?,

    val classNum: Int?,

    val homeroomTeacherId: UUID?,
)
