package com.pickdsm.pickserverspring.domain.afterschool.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.afterschool.api.AfterSchoolApi
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainDeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolNotFoundException
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolStudentNotFound
import com.pickdsm.pickserverspring.domain.afterschool.spi.AfterSchoolSpi

@UseCase
class AfterSchoolUseCase(
    private val afterSchoolSpi: AfterSchoolSpi
) : AfterSchoolApi {

    override fun deleteAfterSchoolStudent(domainDeleteAfterSchoolStudentRequest: DomainDeleteAfterSchoolStudentRequest) {
        val afterSchool = afterSchoolSpi.findByAfterSchoolIdAndStudentId(
            domainDeleteAfterSchoolStudentRequest.afterSchoolId,
            domainDeleteAfterSchoolStudentRequest.studentId
        )

        require(!afterSchool?.studentId!!.equals(null)) { AfterSchoolStudentNotFound }
        require(!afterSchool.id.equals(null)) { AfterSchoolNotFoundException }

        afterSchoolSpi.deleteByAfterSchoolIdAndStudentId(
            afterSchool.id,
            afterSchool.studentId
        )
    }
}
