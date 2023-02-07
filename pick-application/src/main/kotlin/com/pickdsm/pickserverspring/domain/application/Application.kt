package com.pickdsm.pickserverspring.domain.application

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.time.LocalTime
import java.util.UUID

@Aggregate
class Application(

    val statusId: UUID,

    val desiredStartTime: LocalTime,

    val desiredEndTime: LocalTime,

    val reason: String,
)
