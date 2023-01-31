package com.pickdsm.pickserverspring.common.feign.client.dto.response

import com.pickdsm.pickserverspring.global.annotation.NoArg
import java.time.LocalDate
import java.time.LocalTime

@NoArg
data class TimeResponse(
    val date: LocalDate,
    val times: List<TimeElement>,
) {

    @NoArg
    data class TimeElement(
        val id: Int,
        val period: Int,
        val startTime: LocalTime,
        val endTime: LocalTime,
    )
}
