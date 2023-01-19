package com.pickdsm.pickserverspring.global.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.global.error.GlobalErrorCode

object InternalServerErrorException : PickException(
    GlobalErrorCode.INTERNAL_SERVER_ERROR
)
