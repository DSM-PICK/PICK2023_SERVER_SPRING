package com.pickdsm.pickserverspring.domain.application.api.dto.response

import com.pickdsm.pickserverspring.domain.user.dto.UserInfo
import java.time.LocalTime
import java.util.*

data class QueryPicnicApplicationElement(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val reason: String,
    val studentId: UUID,
    val userMap: Map<UUID, UserInfo>,
)
