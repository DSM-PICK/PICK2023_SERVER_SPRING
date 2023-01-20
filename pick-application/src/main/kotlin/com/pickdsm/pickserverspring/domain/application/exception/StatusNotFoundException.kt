package com.pickdsm.pickserverspring.domain.application.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.application.error.StatusErrorCode

object StatusNotFoundException : PickException(
    StatusErrorCode.STATUS_NOT_FOUND,
)
