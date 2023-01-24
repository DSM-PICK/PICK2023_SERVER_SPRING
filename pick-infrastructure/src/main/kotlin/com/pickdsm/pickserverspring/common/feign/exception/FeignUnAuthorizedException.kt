package com.pickdsm.pickserverspring.common.feign.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.common.feign.error.FeignErrorCode

object FeignUnAuthorizedException : PickException(FeignErrorCode.FEIGN_UNAUTHORIZED)
