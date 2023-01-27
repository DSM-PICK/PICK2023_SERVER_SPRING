package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Application
import java.time.LocalDate
import java.util.*

interface QueryApplicationSpi {

    fun queryPicnicApplicationListByToday(date: LocalDate): List<Application>

    fun queryAllStudentIdByToday(date: LocalDate): List<UUID>
}
