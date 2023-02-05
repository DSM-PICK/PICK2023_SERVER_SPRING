package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Status
import java.time.LocalDate
import java.util.*

interface QueryStatusSpi {
    fun queryPicnicStudentInfoListByToday(date: LocalDate): List<Status>

    fun queryMovementStudentInfoListByToday(date: LocalDate): List<Status>
}
