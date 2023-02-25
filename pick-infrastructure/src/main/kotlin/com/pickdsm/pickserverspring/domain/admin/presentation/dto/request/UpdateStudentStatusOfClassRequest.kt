package com.pickdsm.pickserverspring.domain.admin.presentation.dto.request

import com.pickdsm.pickserverspring.domain.application.StatusType
import java.util.UUID

data class UpdateStudentStatusOfClassRequest(
    val userList: List<UpdateStudentElement>,
) {
    data class UpdateStudentElement(
        val userId: UUID,
        val status: StatusType,
    )
}
