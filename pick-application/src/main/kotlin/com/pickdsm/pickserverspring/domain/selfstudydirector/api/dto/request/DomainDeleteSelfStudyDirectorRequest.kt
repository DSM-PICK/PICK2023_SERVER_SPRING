package com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.request

import java.time.LocalDate
import java.util.UUID

data class DomainDeleteSelfStudyDirectorRequest(
    val teacherId: UUID,
    val floor: Int,
    val date: LocalDate,
)
