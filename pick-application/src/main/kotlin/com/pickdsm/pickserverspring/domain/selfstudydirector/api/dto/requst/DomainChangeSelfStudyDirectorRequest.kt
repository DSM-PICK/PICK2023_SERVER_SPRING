package com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.requst

import java.time.LocalDate
import java.util.UUID

data class DomainChangeSelfStudyDirectorRequest(
    val teacherId: UUID,
    val floor: Int,
    val date: LocalDate,
)
