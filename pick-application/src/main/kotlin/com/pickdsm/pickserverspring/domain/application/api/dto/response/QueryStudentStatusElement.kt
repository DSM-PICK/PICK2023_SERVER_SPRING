package com.pickdsm.pickserverspring.domain.application.api.dto.response

import com.pickdsm.pickserverspring.domain.application.StatusType
import java.util.*

data class QueryStudentStatusElement(
    val studentId: UUID,
    val studentNumber: String,
    val studentName: String,
    val type: StatusType,
    val classroomName: String,
)
