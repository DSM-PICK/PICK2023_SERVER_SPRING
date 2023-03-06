package com.pickdsm.pickserverspring.domain.admin.api.dto.response

import java.util.UUID

data class QueryClubStudentList(
    val clubId: UUID,
    val headId: UUID,
    val teacherName: String,
    val classroomName: String,
    val clubName: String,
    val studentList: List<StudentElement>,
) {
    data class StudentElement(
        val studentId: UUID,
        val studentNumber: String,
        val studentName: String,
    )
}
