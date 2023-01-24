package com.pickdsm.pickserverspring.common.feign.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.common.feign.error.FeignErrorCode

object FeignBadRequestException : PickException(FeignErrorCode.FEIGN_BAD_REQUEST)
