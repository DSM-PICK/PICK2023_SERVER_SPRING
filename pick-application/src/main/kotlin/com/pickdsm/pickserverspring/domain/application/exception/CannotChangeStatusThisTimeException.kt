package com.pickdsm.pickserverspring.domain.application.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.application.error.StatusErrorCode

object CannotChangeStatusThisTimeException : PickException(
    StatusErrorCode.CANNOT_CHANGE_STATUS_THIS_TIME,
)