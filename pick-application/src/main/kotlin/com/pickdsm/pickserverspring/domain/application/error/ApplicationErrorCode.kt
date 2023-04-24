package com.pickdsm.pickserverspring.domain.application.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class ApplicationErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    CANNOT_APPLICATION_WEEKEND(401, "Cannot Application Weekend"),

    APPLICATION_NOT_FOUND(404, "Application Not Found"),

    ALREADY_APPLICATION_PICNIC_OR_ALREADY_PICNIC(409, "Already Application Picnic Or Already Picnic"), ;

    override fun status() = status
    override fun message() = message
}
