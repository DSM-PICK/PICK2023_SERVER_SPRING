package com.pickdsm.pickserverspring.domain.admin.presentation.dto.request

import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class PicnicPassRequest(
    @field:NotNull
    val userIdList: List<UUID>,

    @field:NotBlank
    val reason: String,

    @field:NotNull
    val startPeriod: Int,

    @field:NotNull
    val endPeriod: Int,
)
