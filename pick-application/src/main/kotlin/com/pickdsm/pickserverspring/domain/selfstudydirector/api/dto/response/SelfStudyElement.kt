package com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response

import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import java.time.LocalDate

data class SelfStudyElement(
    val type: DirectorType,
    val date: LocalDate,
    val teacher: MutableList<String>,
)
