package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Application
import java.util.UUID

interface QueryApplicationSpi {

    fun queryPicnicApplicationListByToday(): List<Application>

    fun queryApplicationByStudentIdAndStatusId(studentId: UUID, statusId: UUID): Application?
}
