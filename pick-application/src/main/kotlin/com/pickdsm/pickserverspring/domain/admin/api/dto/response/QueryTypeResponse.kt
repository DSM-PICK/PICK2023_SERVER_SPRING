package com.pickdsm.pickserverspring.domain.admin.api.dto.response

import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import java.time.LocalDate
import java.util.UUID

data class QueryTypeResponse(
    val id: UUID,

    val date: LocalDate,

    val type: DirectorType,
)
