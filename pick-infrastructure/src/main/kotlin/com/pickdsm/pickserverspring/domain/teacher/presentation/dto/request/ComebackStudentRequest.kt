package com.pickdsm.pickserverspring.domain.teacher.presentation.dto.request

import java.util.UUID
import javax.validation.constraints.NotNull

data class ComebackStudentRequest(

    @field:NotNull
    val studentId: UUID,

    @field:NotNull
    val endPeriod: Int,
)
