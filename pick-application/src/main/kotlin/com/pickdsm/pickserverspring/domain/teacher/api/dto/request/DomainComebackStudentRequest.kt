package com.pickdsm.pickserverspring.domain.teacher.api.dto.request

import java.util.UUID

data class DomainComebackStudentRequest(
    val studentId: UUID,
    val endPeriod: Int,
)
