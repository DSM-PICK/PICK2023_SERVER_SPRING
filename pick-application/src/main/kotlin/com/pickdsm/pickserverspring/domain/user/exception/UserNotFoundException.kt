package com.pickdsm.pickserverspring.domain.user.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.user.error.UserErrorCode

object UserNotFoundException : PickException(UserErrorCode.USER_NOT_FOUND)
