package com.pickdsm.pickserverspring.domain.teacher.spi

import com.pickdsm.pickserverspring.domain.application.Status
import java.time.LocalDate
import java.util.UUID

interface StatusCommandTeacherSpi {

    fun saveAllStatus(statusList: List<Status>)

    fun saveStatus(status: Status)

    fun saveStatusAndGetStatusId(status: Status): UUID
}
