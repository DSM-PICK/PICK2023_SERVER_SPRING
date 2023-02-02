package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Application
import java.time.LocalDate

interface QueryApplicationSpi {

    fun queryPicnicApplicationListByToday(date: LocalDate): List<Application>

    fun queryApplicationListByToday(date: LocalDate): List<Application>

    fun queryAllApplication(): List<Application>
}
