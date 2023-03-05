package com.pickdsm.pickserverspring.domain.afterschool.api.dto.response

import java.util.UUID

data class QueryAfterSchoolStudentList(
    val afterSchoolName: String,
    val afterSchoolUserList: List<QueryAfterSchoolStudentElement>,
) {
    data class QueryAfterSchoolStudentElement(
        val studentId: UUID,
        val studentNumber: String,
        val studentName: String,
    )
}
