package com.pickdsm.pickserverspring.domain.application.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.application.error.ApplicationErrorCode

object ApplicationNotFoundException : PickException(
    ApplicationErrorCode.APPLICATION_NOT_FOUND
)
