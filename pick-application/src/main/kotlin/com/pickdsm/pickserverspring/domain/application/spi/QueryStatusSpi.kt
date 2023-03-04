package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Status
import java.time.LocalDate
import java.util.UUID

interface QueryStatusSpi {

    fun queryPicnicStudentInfoListByToday(date: LocalDate): List<Status>

    fun queryMovementStudentInfoListByToday(date: LocalDate): List<Status>

    fun queryAwaitStudentListByToday(date: LocalDate): List<Status>

    fun queryStatusListByToday(): List<Status>

    fun queryStatusListByDate(date: LocalDate): List<Status>

    fun queryPicnicStudentByStudentId(studentId: UUID): Status?

    fun queryStatusByStudentIdAndTeacherId(studentId: UUID, teacherId: UUID): Status?
}
