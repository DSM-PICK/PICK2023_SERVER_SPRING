package com.pickdsm.pickserverspring.domain.afterschool.api

import com.pickdsm.pickserverspring.domain.afterschool.api.dto.request.DomainCreateAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.request.DomainDeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.response.QueryAfterSchoolStudentList
import java.util.UUID

interface AfterSchoolApi {

    fun deleteAfterSchoolStudent(domainDeleteAfterSchoolStudentRequest: DomainDeleteAfterSchoolStudentRequest)

    fun createAfterSchoolStudent(request: DomainCreateAfterSchoolStudentRequest)

    fun getAfterSchoolStudents(afterSchoolId: UUID): QueryAfterSchoolStudentList
}
