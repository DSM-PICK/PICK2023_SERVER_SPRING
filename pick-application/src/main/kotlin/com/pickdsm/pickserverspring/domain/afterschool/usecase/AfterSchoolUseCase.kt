package com.pickdsm.pickserverspring.domain.afterschool.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.api.AfterSchoolApi
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainCreateAfterSchoolStudentRequest
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

    override fun createAfterSchoolStudent(request: DomainCreateAfterSchoolStudentRequest) {
        val afterSchool = queryAfterSchoolSpi.findByAfterSchoolId(request.afterSchoolId)
            ?: throw AfterSchoolNotFoundException

        val afterSchools = request.studentIds.map {
            AfterSchool(
                afterSchoolName = afterSchool.afterSchoolName,
                teacherId = afterSchool.teacherId,
                studentId = it,
                classroomId = afterSchool.classroomId,
            )
        }
        commandAfterSchoolSpi.saveAll(afterSchools)
    }
}
