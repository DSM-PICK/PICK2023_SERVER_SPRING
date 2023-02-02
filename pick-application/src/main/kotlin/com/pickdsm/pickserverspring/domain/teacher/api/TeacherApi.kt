package com.pickdsm.pickserverspring.domain.teacher.api

import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryStudentStatusCountResponse

interface TeacherApi {

    fun updateStudentStatus(request: DomainUpdateStudentStatusRequest)

    fun getStudentStatusCount(): QueryStudentStatusCountResponse
}
