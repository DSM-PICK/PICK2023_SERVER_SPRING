package com.pickdsm.pickserverspring.common.error

enum class GlobalErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    BAD_REQUEST(400, "Bad Request"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    override fun status(): Int = status
    override fun message(): String = message
}
