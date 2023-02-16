package com.pickdsm.pickserverspring.domain.afterschool.api

import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainDeleteAfterSchoolStudentRequest

interface AfterSchoolApi {

    fun deleteAfterSchoolStudent(domainDeleteAfterSchoolStudentRequest: DomainDeleteAfterSchoolStudentRequest)
}