package com.pickdsm.pickserverspring.domain.user.dto

import java.util.*

data class UserInfo(
    val id: UUID,
    val grade: Int,
    val classNum: Int,
    val num: Int,
    val studentName: String,
)
