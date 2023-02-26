package com.pickdsm.pickserverspring.domain.admin.api.dto.response

import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import java.time.LocalDate

data class QueryTodayTypeResponse(
    val date: LocalDate,

    val type: DirectorType,
)
