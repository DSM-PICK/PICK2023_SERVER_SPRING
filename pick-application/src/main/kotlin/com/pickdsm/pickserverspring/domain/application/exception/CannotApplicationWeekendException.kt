package com.pickdsm.pickserverspring.domain.application.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.application.error.ApplicationErrorCode

object CannotApplicationWeekendException : PickException(
    ApplicationErrorCode.CANNOT_APPLICATION_WEEKEND,
)
