package com.pickdsm.pickserverspring.domain.admin.api

import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryTodayTypeResponse
import java.time.LocalDate

interface AdminApi {

    fun updateStudentStatusOfClass(request: DomainUpdateStudentStatusOfClassRequest)

    fun queryTypeByDate(date: LocalDate): QueryTodayTypeResponse
}
