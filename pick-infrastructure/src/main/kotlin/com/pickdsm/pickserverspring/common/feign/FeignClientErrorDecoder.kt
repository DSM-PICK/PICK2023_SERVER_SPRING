package com.pickdsm.pickserverspring.common.feign

import com.pickdsm.pickserverspring.common.feign.exception.FeignBadRequestException
import com.pickdsm.pickserverspring.common.feign.exception.FeignExpiredTokenException
import com.pickdsm.pickserverspring.common.feign.exception.FeignForbiddenException
import com.pickdsm.pickserverspring.common.feign.exception.FeignInternalServerError
import com.pickdsm.pickserverspring.common.feign.exception.FeignUnAuthorizedException
import feign.FeignException
import feign.Response
import feign.codec.ErrorDecoder

class FeignClientErrorDecoder : ErrorDecoder {

    override fun decode(methodKey: String, response: Response): Exception {
        if (response.status() >= 400) {
            when (response.status()) {
                400 -> throw FeignBadRequestException
                401 -> throw FeignUnAuthorizedException
                403 -> throw FeignForbiddenException
                419 -> throw FeignExpiredTokenException
                else -> throw FeignInternalServerError
            }
        }

        return FeignException.errorStatus(methodKey, response)
    }
}
