package com.pickdsm.pickserverspring.domain.teacher.api

import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryStatusListResponse
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryStudentStatusCountResponse
import java.time.LocalDate
import java.util.*

interface TeacherApi {

    fun updateStudentStatus(request: DomainUpdateStudentStatusRequest)

    fun getStudentStatusCount(): QueryStudentStatusCountResponse

    //fun queryStatus(date: LocalDate): QueryStatusListResponse

    fun statusPicnicApplication(studentId: UUID)
}
