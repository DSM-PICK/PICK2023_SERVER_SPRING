package com.pickdsm.pickserverspring.domain.application.api.dto.response

import java.time.LocalTime
import java.util.UUID

data class QueryPicnicApplicationElement(
    val studentId: UUID,
    val studentNumber: String,
    val studentName: String,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val reason: String,
)
