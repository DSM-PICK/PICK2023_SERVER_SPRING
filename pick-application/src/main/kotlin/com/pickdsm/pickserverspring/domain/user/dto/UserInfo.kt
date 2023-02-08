package com.pickdsm.pickserverspring.domain.user.dto

import java.util.UUID

data class UserInfo(
    val id: UUID,
    val profileFileName: String,
    val num: Int,
    val name: String,
)
