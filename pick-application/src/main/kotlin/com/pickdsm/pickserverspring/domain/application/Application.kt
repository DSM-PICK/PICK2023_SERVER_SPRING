package com.pickdsm.pickserverspring.domain.application

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Aggregate
class Application(

    val id: UUID = UUID.randomUUID(),

    val studentId: UUID,

    val date: LocalDate,

    val startTime: LocalTime,

    val endTime: LocalTime,

    val reason: String,

    val isStatus: Boolean = false,

    val isPermission: Boolean = false,
)
