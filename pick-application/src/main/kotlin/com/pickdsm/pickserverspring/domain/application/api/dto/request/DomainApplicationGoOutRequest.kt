package com.pickdsm.pickserverspring.domain.application.api.dto.request

import java.time.LocalTime

data class DomainApplicationGoOutRequest(
    val desiredStartTime: LocalTime,
    val desiredEndTime: LocalTime,
    val reason: String,
)
