package com.pickdsm.pickserverspring.domain.time

import com.pickdsm.pickserverspring.common.feign.client.TimeClient
import com.pickdsm.pickserverspring.domain.application.exception.CannotChangeStatusThisTimeException
import com.pickdsm.pickserverspring.domain.time.Time.DomainTimeElement
import com.pickdsm.pickserverspring.domain.time.spi.TimeSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import java.time.LocalDate
import java.time.LocalTime

@Adapter
class TimePersistenceAdapter(
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

    override fun queryNowPeriod(time: Time): Int {
        val timeNow = LocalTime.now()
        val filterTime = time.timeList.find { timeNow.isAfter(it.startTime) && timeNow.isBefore(it.endTime) }
        return filterTime?.period ?: throw CannotChangeStatusThisTimeException
    }
}
