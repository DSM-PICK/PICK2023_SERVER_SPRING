package com.pickdsm.pickserverspring.domain.teacher.api

import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest

interface TeacherApi {

    fun updateStudentStatus(request: DomainUpdateStudentStatusRequest)
}
