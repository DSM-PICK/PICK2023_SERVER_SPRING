package com.pickdsm.pickserverspring.domain.user.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class UserErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    USER_NOT_FOUND(404, "User not found"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
