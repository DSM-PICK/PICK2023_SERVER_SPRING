package com.pickdsm.pickserverspring.domain.schedule.spi.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.schedule.spi.error.ScheduleErrorCode

object HomecomingDayException : PickException(
    ScheduleErrorCode.HOMECOMING_DAY,
)
