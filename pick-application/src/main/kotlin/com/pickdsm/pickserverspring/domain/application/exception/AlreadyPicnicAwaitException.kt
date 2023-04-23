package com.pickdsm.pickserverspring.domain.application.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.application.error.ApplicationErrorCode

object AlreadyPicnicAwaitException : PickException (
        ApplicationErrorCode.AlREADY_PICNIC_AWAIT
)