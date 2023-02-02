package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Status
import java.time.LocalDate

interface QueryStatusSpi {

    fun getAllPicnicStatus(): List<Status>

    fun queryPicnicStudentInfoListByToday(date: LocalDate): List<Status>
}
