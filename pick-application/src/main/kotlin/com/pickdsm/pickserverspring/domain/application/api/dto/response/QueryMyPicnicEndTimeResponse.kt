package com.pickdsm.pickserverspring.domain.application.api.dto.response

import java.time.LocalTime
import java.util.UUID

data class QueryMyPicnicEndTimeResponse(
    val userId: UUID,
    val name: String,
    val endTime: LocalTime,
)
