package com.pickdsm.pickserverspring.domain.time

import java.time.LocalDate
import java.time.LocalTime

data class Time(
    val date: LocalDate,
    val timeList: List<DomainTimeElement>,
) {
    data class DomainTimeElement(
        val id: Int,
        val period: Int,
        val startTime: LocalTime,
        val endTime: LocalTime,
    )
}
