package com.pickdsm.pickserverspring.domain.afterschool.presentation.dto.requset

import java.util.UUID

data class DeleteAfterSchoolStudentRequest(
    val afterSchoolId: UUID,
    val studentId: UUID,
)
