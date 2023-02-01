package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Status
import java.time.LocalDate
import java.util.UUID

interface QueryStatusSpi {
    fun queryPicnicStudentIdListByToday(date: LocalDate): List<UUID>

    fun queryPicnicStudentInfoListByToday(date: LocalDate): List<Status>
}
