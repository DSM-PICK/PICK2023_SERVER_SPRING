package com.pickdsm.pickserverspring.domain.classroom.api.dto.response

data class MovementStudentElement(
    val studentNumber: String,
    val studentName: String,
    val before: String,
    val after: String,
)
