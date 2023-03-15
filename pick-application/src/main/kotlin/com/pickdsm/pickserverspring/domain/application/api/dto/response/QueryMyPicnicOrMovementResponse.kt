package com.pickdsm.pickserverspring.domain.application.api.dto.response

import java.time.LocalTime

data class QueryMyPicnicOrMovementResponse(
    val name: String,
    val endTime: LocalTime,
    val classroomName: String,
)
