package com.pickdsm.pickserverspring.domain.afterschool.presentation.dto.requset

import java.util.UUID

data class CreateAfterSchoolStudentRequest(
    val userIdList: List<UUID>,
)
