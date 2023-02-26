package com.pickdsm.pickserverspring.domain.afterschool.api

import com.pickdsm.pickserverspring.domain.afterschool.api.dto.request.DomainCreateAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.request.DomainDeleteAfterSchoolStudentRequest

interface AfterSchoolApi {

    fun deleteAfterSchoolStudent(domainDeleteAfterSchoolStudentRequest: DomainDeleteAfterSchoolStudentRequest)

    fun createAfterSchoolStudent(request: DomainCreateAfterSchoolStudentRequest)
}
