package com.pickdsm.pickserverspring.domain.afterschool.api.dto

import java.util.UUID

data class DomainCreateAfterSchoolStudentRequest(
    val afterSchoolId: UUID,
    val studentIds: List<UUID>,
)
