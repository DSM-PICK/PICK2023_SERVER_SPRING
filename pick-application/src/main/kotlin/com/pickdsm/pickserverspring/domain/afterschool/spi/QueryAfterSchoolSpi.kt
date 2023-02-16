package com.pickdsm.pickserverspring.domain.afterschool.spi

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import java.util.UUID

interface QueryAfterSchoolSpi {

    fun findByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID): AfterSchool?
}
