package com.pickdsm.pickserverspring.domain.teacher.api.dto.response

import java.util.UUID

data class QueryMovementStudentList(
    val movementStudentList: List<MovementStudent>,
) {
    data class MovementStudent(
        val studentId: UUID,
        val studentNumber: String,
        val studentName: String,
        val type: String,
        val classroomName: String,
    )
}
