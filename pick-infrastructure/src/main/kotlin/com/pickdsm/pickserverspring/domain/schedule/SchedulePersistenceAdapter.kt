package com.pickdsm.pickserverspring.domain.schedule

import com.pickdsm.pickserverspring.common.feign.client.ScheduleClient
import com.pickdsm.pickserverspring.domain.schedule.spi.QueryScheduleSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter

@Adapter
class SchedulePersistenceAdapter(
    private val scheduleClient: ScheduleClient,
) : QueryScheduleSpi {

    override fun queryIsHomecomingDay(date: String): Boolean {
        val scheduleResponse = scheduleClient.getIsHomecomingDay(date)

        return scheduleResponse.isHomecomingDay
    }
}
