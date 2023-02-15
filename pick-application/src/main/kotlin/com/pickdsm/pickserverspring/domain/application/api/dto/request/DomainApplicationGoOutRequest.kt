package com.pickdsm.pickserverspring.domain.application.api.dto.request

data class DomainApplicationGoOutRequest(
    val desiredStartPeriod: Int,
    val desiredEndPeriod: Int,
    val reason: String,
)
