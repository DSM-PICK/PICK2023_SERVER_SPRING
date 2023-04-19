package com.pickdsm.pickserverspring.domain.schedule.spi.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class ScheduleErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    SCHEDULE_NOT_FOUND_ON_HOMECOMING_DAY(404, "Schedule not found on homecoming day"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
