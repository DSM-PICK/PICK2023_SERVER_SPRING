package com.pickdsm.pickserverspring.domain.teacher.presentation.dto.request

import com.pickdsm.pickserverspring.domain.application.StatusType
import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class PicnicAcceptOrRefuseRequest(
    @field:NotNull
    val type: StatusType,

    @field:NotNull
    val userIdList: List<UUID>,
)
