package com.pickdsm.pickserverspring.domain.selfstudydirector.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.selfstudydirector.error.TypeErrorCode

object TypeNotFoundException : PickException(
    TypeErrorCode.TYPE_NOT_FOUND,
)
