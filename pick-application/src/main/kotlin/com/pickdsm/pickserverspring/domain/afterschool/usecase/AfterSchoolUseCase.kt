package com.pickdsm.pickserverspring.domain.afterschool.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.afterschool.api.AfterSchoolApi
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainDeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolNotFoundException
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolStudentNotFoundException
import com.pickdsm.pickserverspring.domain.afterschool.spi.CommandAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi

@UseCase
class AfterSchoolUseCase(
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
    private val commandAfterSchoolSpi: CommandAfterSchoolSpi,
) : AfterSchoolApi {

    override fun deleteAfterSchoolStudent(domainDeleteAfterSchoolStudentRequest: DomainDeleteAfterSchoolStudentRequest) {
        val afterSchool = queryAfterSchoolSpi.findByAfterSchoolIdAndStudentId(
            domainDeleteAfterSchoolStudentRequest.afterSchoolId,
            domainDeleteAfterSchoolStudentRequest.studentId,
        )

        require(!afterSchool?.studentId!!.equals(null)) { AfterSchoolStudentNotFoundException }
        require(!afterSchool.id.equals(null)) { AfterSchoolNotFoundException }

        commandAfterSchoolSpi.deleteByAfterSchoolIdAndStudentId(
            afterSchool.id,
            afterSchool.studentId,
        )
    }
}
