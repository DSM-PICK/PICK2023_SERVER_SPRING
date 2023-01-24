package com.pickdsm.pickserverspring.common.feign.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.common.feign.error.FeignErrorCode

object FeignExpiredTokenException : PickException(FeignErrorCode.FEIGN_EXPIRED_TOKEN)
