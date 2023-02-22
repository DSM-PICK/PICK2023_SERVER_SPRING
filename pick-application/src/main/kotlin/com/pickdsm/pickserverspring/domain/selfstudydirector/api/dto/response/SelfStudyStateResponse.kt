package com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response

import java.time.LocalDate

data class SelfStudyStateResponse(
    val date: LocalDate,
    val name: String,
    val floor: List<Int>,
)
