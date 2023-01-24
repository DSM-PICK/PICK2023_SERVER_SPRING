package com.pickdsm.pickserverspring.common.feign.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.common.feign.error.FeignErrorCode

object FeignForbiddenException : PickException(FeignErrorCode.FEIGN_FORBIDDEN)
