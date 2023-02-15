package com.pickdsm.pickserverspring.domain.application.presentation.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ApplicationGoOutRequest(

    @field:NotNull
    val desiredStartPeriod: Int,

    @field:NotNull
    val desiredEndPeriod: Int,

    @field:NotBlank
    val reason: String,
)
