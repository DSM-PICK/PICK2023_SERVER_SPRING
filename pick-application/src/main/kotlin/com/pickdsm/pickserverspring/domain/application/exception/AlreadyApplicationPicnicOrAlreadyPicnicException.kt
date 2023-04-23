package com.pickdsm.pickserverspring.domain.application.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.application.error.ApplicationErrorCode

object AlreadyApplicationPicnicOrAlreadyPicnicException : PickException(
    ApplicationErrorCode.ALREADY_APPLICATION_PICNIC_OR_ALREADY_PICNIC,
)
