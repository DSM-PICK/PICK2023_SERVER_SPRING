package com.pickdsm.pickserverspring.domain.application.api.dto.response

import java.util.UUID

data class QueryStudentStatusElement(
    val studentId: UUID,
    val studentNumber: String,
    val studentName: String,
    val type: String,
    val classroomName: String,
)
