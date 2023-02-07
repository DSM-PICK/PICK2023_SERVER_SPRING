package com.pickdsm.pickserverspring.domain.application.presentation.dto.request

import java.time.LocalTime
import javax.validation.constraints.NotNull

data class ApplicationGoOutRequest(

    @field:NotNull
    val desiredStartTime: LocalTime,

    @field:NotNull
    val desiredEndTime: LocalTime,

    @field:NotNull
    val reason: String,
)
