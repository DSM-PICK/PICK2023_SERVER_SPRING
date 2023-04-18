package com.pickdsm.pickserverspring.common.feign.client

import com.pickdsm.pickserverspring.common.feign.client.dto.response.ScheduleResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "ScheduleClient", url = "\${service.scheme}://\${service.schedule.host}")
interface ScheduleClient {

    @GetMapping("/schedules/school/is-homecoming")
    fun getIsHomecomingDay(@RequestParam("date") date: String): ScheduleResponse
}
