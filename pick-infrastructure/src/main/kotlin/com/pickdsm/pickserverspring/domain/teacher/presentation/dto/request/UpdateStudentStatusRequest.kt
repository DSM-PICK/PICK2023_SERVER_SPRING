package com.pickdsm.pickserverspring.domain.teacher.presentation.dto.request

import com.pickdsm.pickserverspring.domain.application.StatusType
import java.util.UUID

data class UpdateStudentStatusRequest(
    val period: Int,
    val userId: UUID,
    val status: StatusType,
)
