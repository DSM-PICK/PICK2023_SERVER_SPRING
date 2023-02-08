package com.pickdsm.pickserverspring.domain.selfstudydirector.spi

import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector
import java.time.LocalDate
import java.util.UUID

interface QuerySelfStudyDirectorSpi {

    fun querySelfStudyDirectorByDate(date: LocalDate): List<SelfStudyDirector>

    fun querySelfStudyDirectorTeacherIdByDate(date: LocalDate): List<UUID>

    fun queryResponsibleFloorByTeacherId(teacherId: UUID): Int?
}
