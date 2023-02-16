package com.pickdsm.pickserverspring.domain.afterschool.api.dto

import java.util.UUID

data class DomainDeleteAfterSchoolStudentRequest(
    val afterSchoolId: UUID,
    val studentId: UUID,
)
