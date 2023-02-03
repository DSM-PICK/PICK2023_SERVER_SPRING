package com.pickdsm.pickserverspring.domain.teacher.presentation.dto.request

import com.pickdsm.pickserverspring.domain.application.StatusType
import java.util.UUID
import javax.validation.constraints.NotNull

data class UpdateStudentStatusRequest(
    val period: Int,
    val userList: List<UpdateStudentStatusElement>,
) {
    data class UpdateStudentStatusElement(
        @field:NotNull
        val userId: UUID,

        @field:NotNull
        val status: StatusType,
    )
}
