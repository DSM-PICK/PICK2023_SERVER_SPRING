package com.pickdsm.pickserverspring.domain.application.api.dto.request

import java.time.LocalTime

data class DomainApplicationGoOutRequest(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val reason: String,
)
