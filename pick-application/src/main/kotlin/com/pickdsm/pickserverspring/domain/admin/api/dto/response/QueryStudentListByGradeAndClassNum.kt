package com.pickdsm.pickserverspring.domain.admin.api.dto.response

import com.pickdsm.pickserverspring.domain.application.StatusType
import java.util.UUID

data class QueryStudentListByGradeAndClassNum(
    val teacherName: String,
    val studentList: List<StudentElementByGradeAndClassNum>,
) {
    data class StudentElementByGradeAndClassNum(
        val studentId: UUID,
        val studentNumber: Int,
        val studentName: String,
        val status: StatusType,
    )
}
