package com.pickdsm.pickserverspring.domain.afterschool.spi

import java.util.UUID

interface CommandAfterSchoolSpi {

    fun deleteByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID)
}
