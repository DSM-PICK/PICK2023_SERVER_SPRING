package com.pickdsm.pickserverspring.domain.application.presentation.dto.request

import java.util.UUID

data class ApplicationUserIdsRequest(
    val userIdList: List<UUID>,
)
