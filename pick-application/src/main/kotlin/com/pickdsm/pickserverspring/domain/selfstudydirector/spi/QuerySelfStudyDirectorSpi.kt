package com.pickdsm.pickserverspring.domain.selfstudydirector.spi

import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector
import java.time.LocalDate
import java.util.UUID

interface QuerySelfStudyDirectorSpi {

    fun querySelfStudyDirectorByDate(date: LocalDate): List<SelfStudyDirector>

    fun queryResponsibleFloorByTeacherIdAndToday(teacherId: UUID): Int?

    fun queryResponsibleFloorByTeacherIdAndTypeId(teacherId: UUID, typeId: UUID): Int?

    fun queryAllSelfStudyDirectorByTeacherIdAndDate(teacherId: UUID, date: LocalDate): List<SelfStudyDirector>

    fun querySelfStudyDirectorByTeacherId(teacherId: UUID): SelfStudyDirector

    fun querySelfStudyDirectorByDateAndFloor(date: LocalDate, floor: Int): SelfStudyDirector?

    fun querySelfStudyDirectorByToday(): List<SelfStudyDirector>
}
