package com.pickdsm.pickserverspring.domain.application

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.time.LocalDate
import java.util.UUID

@Aggregate
class Status (

    val id: UUID = UUID(0,0),

    val type: String,

    val date: LocalDate
)