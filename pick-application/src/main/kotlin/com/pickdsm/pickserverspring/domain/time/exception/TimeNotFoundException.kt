package com.pickdsm.pickserverspring.domain.time.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.time.error.TimeErrorCode

object TimeNotFoundException : PickException(TimeErrorCode.TIME_NOT_FOUND)
