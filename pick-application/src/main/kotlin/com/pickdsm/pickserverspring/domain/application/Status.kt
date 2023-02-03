package com.pickdsm.pickserverspring.domain.application

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Aggregate
class Status(

    val id: UUID = UUID.randomUUID(),

    val studentId: UUID,

    val teacherId: UUID,

    val type: StatusType,

    val date: LocalDate = LocalDate.now(),

    val startTime: LocalTime,

    val endTime: LocalTime,
)
