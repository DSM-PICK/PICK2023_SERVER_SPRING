package com.pickdsm.pickserverspring.domain.application.api.dto.response

import java.time.LocalTime
import java.util.UUID

data class QueryPicnicStudentElement(
    val studentId: UUID,
    val studentNumber: String,
    val studentName: String,
    val endTime: LocalTime,
)
