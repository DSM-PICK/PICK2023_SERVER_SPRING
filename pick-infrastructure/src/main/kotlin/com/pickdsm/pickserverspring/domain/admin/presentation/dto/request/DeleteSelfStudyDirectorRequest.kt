package com.pickdsm.pickserverspring.domain.admin.presentation.dto.request

import java.time.LocalDate
import java.util.*

data class DeleteSelfStudyDirectorRequest(
    val teacherId: UUID,
    val floor: Int,
    val date: LocalDate,
)

