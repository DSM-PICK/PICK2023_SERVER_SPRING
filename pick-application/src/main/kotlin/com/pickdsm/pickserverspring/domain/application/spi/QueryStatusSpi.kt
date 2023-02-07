package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Status
import java.time.LocalDate

interface QueryStatusSpi {

    fun queryPicnicStudentInfoListByToday(date: LocalDate): List<Status>

    fun queryMovementStudentInfoListByToday(date: LocalDate): List<Status>

    fun queryAwaitStudentListByToday(date: LocalDate): List<Status>
}
