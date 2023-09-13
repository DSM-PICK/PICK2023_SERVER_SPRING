package com.pickdsm.pickserverspring.domain.user.dto.request

import java.util.UUID

data class UserInfoRequest(
    val userIds: List<UUID>,
)
