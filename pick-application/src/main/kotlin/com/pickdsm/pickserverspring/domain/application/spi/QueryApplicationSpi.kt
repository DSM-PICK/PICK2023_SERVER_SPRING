package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Application
import java.time.LocalDate
import java.util.UUID

interface QueryApplicationSpi {

    fun queryPicnicApplicationListByToday(date: LocalDate): List<Application>

    fun queryApplicationListByToday(date: LocalDate): List<Application>

    fun queryApplicationByStudentIdAndStatusId(studentId: UUID, statusId: UUID): Application?
}
