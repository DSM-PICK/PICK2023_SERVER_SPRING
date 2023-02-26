package com.pickdsm.pickserverspring.domain.admin.api

import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentAttendanceList
import java.time.LocalDate
import java.util.UUID

interface AdminApi {

    fun updateStudentStatusOfClass(request: DomainUpdateStudentStatusOfClassRequest)

    fun getStudentAttendanceList(classroomId: UUID, date: LocalDate): QueryStudentAttendanceList
}
