package com.pickdsm.pickserverspring.domain.application.presentation.dto.request

import java.time.LocalTime

data class ApplicationGoOutRequest(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val reason: String,
)
