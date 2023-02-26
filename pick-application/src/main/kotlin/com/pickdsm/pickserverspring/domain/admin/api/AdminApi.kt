package com.pickdsm.pickserverspring.domain.admin.api

import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest

interface AdminApi {

    fun updateStudentStatusOfClass(request: DomainUpdateStudentStatusOfClassRequest)
}
