package com.pickdsm.pickserverspring.domain.teacher.spi

import com.pickdsm.pickserverspring.domain.time.Time
import java.time.LocalDate

interface TimeQueryTeacherSpi {

    fun queryTime(date: LocalDate): Time
}
