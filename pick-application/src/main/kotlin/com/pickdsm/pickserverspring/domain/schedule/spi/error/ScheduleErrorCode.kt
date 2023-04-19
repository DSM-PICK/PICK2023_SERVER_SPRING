package com.pickdsm.pickserverspring.domain.schedule.spi.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class ScheduleErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    HOMECOMING_DAY(404, "homecoming day"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
