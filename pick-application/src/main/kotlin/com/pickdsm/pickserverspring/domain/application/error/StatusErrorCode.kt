package com.pickdsm.pickserverspring.domain.application.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class StatusErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    CANNOT_CHANGE_EMPLOYMENT(401, "Cannot Change Employment"),
    CANNOT_CHANGE_STATUS_THIS_TIME(401, "Cannot Change Status This Time"),

    STATUS_NOT_FOUND(404, "Status not found"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
