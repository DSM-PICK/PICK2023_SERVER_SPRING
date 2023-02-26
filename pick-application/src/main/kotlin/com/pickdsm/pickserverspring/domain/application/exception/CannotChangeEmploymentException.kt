package com.pickdsm.pickserverspring.domain.application.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.application.error.StatusErrorCode

object CannotChangeEmploymentException : PickException(
    StatusErrorCode.CANNOT_CHANGE_EMPLOYMENT,
)
