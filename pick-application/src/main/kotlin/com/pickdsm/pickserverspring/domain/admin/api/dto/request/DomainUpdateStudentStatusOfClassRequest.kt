package com.pickdsm.pickserverspring.domain.admin.api.dto.request

import com.pickdsm.pickserverspring.domain.application.StatusType
import java.util.UUID

data class DomainUpdateStudentStatusOfClassRequest(
    val userList: List<DomainUpdateStudentElement>,
) {
    data class DomainUpdateStudentElement(
        val userId: UUID,
        val status: StatusType,
    )
}
