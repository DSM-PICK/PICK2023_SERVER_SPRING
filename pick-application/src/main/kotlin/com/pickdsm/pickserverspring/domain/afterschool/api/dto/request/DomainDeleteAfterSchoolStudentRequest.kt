package com.pickdsm.pickserverspring.domain.afterschool.api.dto.request

import java.util.UUID

data class DomainDeleteAfterSchoolStudentRequest(
    val afterSchoolId: UUID,
    val studentId: UUID,
)
