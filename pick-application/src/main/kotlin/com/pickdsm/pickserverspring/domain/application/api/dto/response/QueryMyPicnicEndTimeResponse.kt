package com.pickdsm.pickserverspring.domain.application.api.dto.response

import java.time.LocalTime

data class QueryMyPicnicEndTimeResponse(
    val name: String,
    val endTime: LocalTime,
)
