package com.pickdsm.pickserverspring.domain.afterschool.spi

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import java.util.UUID

interface QueryAfterSchoolSpi {

    fun queryAfterSchoolList(): List<AfterSchool>

    fun findByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID): AfterSchool?
}
