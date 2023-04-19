package com.pickdsm.pickserverspring.common.feign.client.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ScheduleResponse(

    @JsonProperty("isHomecomingDay")
    val isHomecomingDay: Boolean,
)
