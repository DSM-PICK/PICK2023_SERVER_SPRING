package com.pickdsm.pickserverspring.domain.admin.presentation.dto.request

import java.util.UUID
import javax.validation.constraints.NotNull

data class ChangeClubStudentRequest(
    @field:NotNull
    val clubInfoId: UUID,
    @field:NotNull
    val studentId: UUID,
)
