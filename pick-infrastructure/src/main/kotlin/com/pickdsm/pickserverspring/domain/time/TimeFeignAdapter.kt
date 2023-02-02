package com.pickdsm.pickserverspring.domain.time

import com.pickdsm.pickserverspring.common.feign.client.TimeClient
import com.pickdsm.pickserverspring.domain.time.Time.DomainTimeElement
import com.pickdsm.pickserverspring.domain.time.spi.TimeSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import java.time.LocalDate

@Adapter
class TimeFeignAdapter(
    private val timeClient: TimeClient,
) : TimeSpi {

    override fun queryTime(date: LocalDate): Time {
        val timeList = timeClient.getTime(date.toString())
        val timeElementList = timeList.times.map {
            DomainTimeElement(
                id = it.id,
                period = it.period,
                startTime = it.beginTime,
                endTime = it.endTime,
            )
        }
        return Time(
            date = timeList.date,
            timeList = timeElementList,
        )
    }
}
