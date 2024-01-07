package com.pickdsm.pickserverspring.domain.user

import java.time.LocalDate
import java.util.UUID

data class User(
    val id: UUID,
    val accountId: String,
    val password: String,
    val name: String,
    val grade: Int,
    val classNum: Int,
    val num: Int,
    val birthDay: LocalDate,
    val profileFileName: String?,
) {
    companion object {
        fun User.processGcn() = "${this.grade}${this.classNum}${this.paddedUserNum()}"
        private fun User.paddedUserNum(): String = this.num.toString().padStart(2, '0')
    }
}
