package com.pickdsm.pickserverspring.domain.application.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.application.error.StatusErrorCode

object OverEndTimeException : PickException(
    StatusErrorCode.OVER_END_TIME,
)
