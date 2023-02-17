package com.pickdsm.pickserverspring.domain.afterschool.spi

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import java.util.UUID

interface CommandAfterSchoolSpi {

    fun deleteByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID)

    fun saveAll(afterSchools: List<AfterSchool>)
}
