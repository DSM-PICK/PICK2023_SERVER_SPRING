package com.pickdsm.pickserverspring.domain.afterschool.api

import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainCreateAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainDeleteAfterSchoolStudentRequest

interface AfterSchoolApi {

    fun deleteAfterSchoolStudent(domainDeleteAfterSchoolStudentRequest: DomainDeleteAfterSchoolStudentRequest)

    fun createAfterSchoolStudent(request: DomainCreateAfterSchoolStudentRequest)
}
