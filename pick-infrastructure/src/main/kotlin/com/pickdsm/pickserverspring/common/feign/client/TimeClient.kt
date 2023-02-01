package com.pickdsm.pickserverspring.common.feign.client

import com.pickdsm.pickserverspring.common.feign.client.dto.response.TimeResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "TimetableClient", url = "\${service.scheme}://\${service.timetable.host}")
interface TimeClient {

    @GetMapping("/{date}")
    fun getTime(@PathVariable("date") date: String): TimeResponse
}
