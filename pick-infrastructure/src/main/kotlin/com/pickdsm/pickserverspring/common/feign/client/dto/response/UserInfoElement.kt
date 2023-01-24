package com.pickdsm.pickserverspring.common.feign.client.dto.response

import java.time.LocalDate
import java.util.*

data class UserInfoElement(
    val id: UUID,
    val accountId: String,
    val password: String,
    val name: String,
    val grade: Int,
    val classNum: Int,
    val num: Int,
    val birthDay: LocalDate,
    val profileFileName: String,
)
