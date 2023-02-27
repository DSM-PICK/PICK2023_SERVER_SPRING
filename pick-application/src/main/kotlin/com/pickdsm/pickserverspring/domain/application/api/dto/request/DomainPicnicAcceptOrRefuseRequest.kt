package com.pickdsm.pickserverspring.domain.application.api.dto.request

import com.pickdsm.pickserverspring.domain.application.StatusType
import java.util.UUID

data class DomainPicnicAcceptOrRefuseRequest(
    val type: StatusType,
    val userIdList: List<UUID>,
)
