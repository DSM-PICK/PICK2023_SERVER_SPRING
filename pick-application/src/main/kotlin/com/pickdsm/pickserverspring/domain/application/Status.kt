package com.pickdsm.pickserverspring.domain.application

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Aggregate
class Status(

    val id: UUID = UUID(0, 0),

    val studentId: UUID,

    val teacherId: UUID,

    val type: StatusType,

    val date: LocalDate = LocalDate.now(),

    val startTime: LocalTime,

    val endTime: LocalTime,
)
