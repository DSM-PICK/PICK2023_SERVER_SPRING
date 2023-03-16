package com.pickdsm.pickserverspring.domain.classroom.api.dto.response

data class ClassroomMovementStudentElement(
    val studentNumber: String,
    val studentName: String,
    val before: String,
    val type: String,
)
