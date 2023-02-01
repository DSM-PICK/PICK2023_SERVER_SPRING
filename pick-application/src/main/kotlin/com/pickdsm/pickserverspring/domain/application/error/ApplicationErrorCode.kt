package com.pickdsm.pickserverspring.domain.application.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class ApplicationErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    APPLICATION_NOT_FOUND(404, "Application Not Found"), ;

    override fun status() = status
    override fun message() = message
}
