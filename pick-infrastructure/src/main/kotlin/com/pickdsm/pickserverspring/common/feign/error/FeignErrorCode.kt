package com.pickdsm.pickserverspring.common.feign.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class FeignErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    FEIGN_BAD_REQUEST(400, "Feign Bad Request"),
    FEIGN_UNAUTHORIZED(401, "Feign Unauthorized"),
    FEIGN_FORBIDDEN(403, "Feign Forbidden"),
    FEIGN_EXPIRED_TOKEN(419, "Feign Expired Token"),
    FEIGN_INTERNAL_SERVER_ERROR(500, "Feign Internal Server Error"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
