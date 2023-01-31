package com.pickdsm.pickserverspring.domain.time.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class TimeErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    TIME_NOT_FOUND(404, "Time not found"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
