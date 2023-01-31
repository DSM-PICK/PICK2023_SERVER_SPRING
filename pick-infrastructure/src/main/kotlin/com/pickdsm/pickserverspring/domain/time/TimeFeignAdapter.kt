package com.pickdsm.pickserverspring.domain.time

import com.pickdsm.pickserverspring.common.feign.client.TimeClient
import com.pickdsm.pickserverspring.domain.time.Time.DomainTimeElement
import com.pickdsm.pickserverspring.domain.time.spi.TimeSpi
import java.time.LocalDate

class TimeFeignAdapter(
    private val timeClient: TimeClient,
) : TimeSpi {

    override fun queryTime(date: LocalDate): Time {
        val timeList = timeClient.getTime(date)
        val timeElementList = timeList.times.map {
            DomainTimeElement(
                id = it.id,
                period = it.period,
                startTime = it.startTime,
                endTime = it.endTime,
            )
        }
        return Time(
            date = timeList.date,
            timeList = timeElementList,
        )
    }
}
