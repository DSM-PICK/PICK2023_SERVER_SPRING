package com.pickdsm.pickserverspring.common.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.common.error.GlobalErrorCode

object InternalServerErrorException : PickException(
    GlobalErrorCode.INTERNAL_SERVER_ERROR
)
