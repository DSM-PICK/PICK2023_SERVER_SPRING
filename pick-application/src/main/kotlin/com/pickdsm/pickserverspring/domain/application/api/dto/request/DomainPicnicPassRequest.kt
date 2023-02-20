package com.pickdsm.pickserverspring.domain.application.api.dto.request

import java.util.*

data class DomainPicnicPassRequest(
    val userIdList: List<UUID>,

    val reason: String,

    val startPeriod: Int,

    val endPeriod: Int,
)
