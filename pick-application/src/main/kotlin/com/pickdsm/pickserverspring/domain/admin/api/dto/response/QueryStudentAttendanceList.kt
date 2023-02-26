package com.pickdsm.pickserverspring.domain.admin.api.dto.response

import com.pickdsm.pickserverspring.domain.application.StatusType
import java.util.*

data class QueryStudentAttendanceList(
    val classroom: String,
    val studentList: List<StudentElement>,
) {
    data class StudentElement(
        val studentId: UUID,
        val studentNumber: String,
        val studentName: String,
        val typeList: List<StatusType>
    )
}
