package com.pickdsm.pickserverspring.domain.user

import java.time.LocalDate
import java.util.*

class User(
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