package com.pickdsm.pickserverspring.domain.application.api.dto.response

import java.time.LocalTime

data class QueryMyPicnicInfoResponse(
    val studentNumber: String,
    val studentName: String,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val reason: String,
    val teacherName: String,
)
