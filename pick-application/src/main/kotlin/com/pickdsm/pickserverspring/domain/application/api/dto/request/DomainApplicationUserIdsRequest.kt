package com.pickdsm.pickserverspring.domain.application.api.dto.request

import java.util.UUID

data class DomainApplicationUserIdsRequest(
    val userIdList: List<UUID>,
)
