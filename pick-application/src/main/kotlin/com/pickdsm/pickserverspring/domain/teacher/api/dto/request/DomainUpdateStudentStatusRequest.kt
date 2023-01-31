package com.pickdsm.pickserverspring.domain.teacher.api.dto.request

import com.pickdsm.pickserverspring.domain.application.StatusType
import java.util.UUID

data class DomainUpdateStudentStatusRequest(
    val period: Int,
    val userList: List<DomainUpdateStudentStatusElement>,
) {
    data class DomainUpdateStudentStatusElement(
        val userId: UUID,
        val status: StatusType
    )
}
