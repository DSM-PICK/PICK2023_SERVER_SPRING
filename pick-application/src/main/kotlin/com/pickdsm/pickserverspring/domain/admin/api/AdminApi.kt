package com.pickdsm.pickserverspring.domain.admin.api

import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentAttendanceList
import java.util.UUID
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryTypeResponse
import java.time.LocalDate

interface AdminApi {

    fun updateStudentStatusOfClass(request: DomainUpdateStudentStatusOfClassRequest)

    fun getStudentAttendanceList(classroomId: UUID, date: LocalDate): QueryStudentAttendanceList

    fun getTypeByDate(date: LocalDate): QueryTypeResponse
}
