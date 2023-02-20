package com.pickdsm.pickserverspring.domain.admin.presentation.dto.request

import java.util.UUID

data class DeleteAfterSchoolStudentRequest(
    val afterSchoolId: UUID,
    val studentId: UUID,
)
