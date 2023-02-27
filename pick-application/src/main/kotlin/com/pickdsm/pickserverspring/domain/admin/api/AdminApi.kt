package com.pickdsm.pickserverspring.domain.admin.api

import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryTypeResponse
import java.time.LocalDate

interface AdminApi {

    fun updateStudentStatusOfClass(request: DomainUpdateStudentStatusOfClassRequest)

    fun getTypeByDate(date: LocalDate): QueryTypeResponse
}
