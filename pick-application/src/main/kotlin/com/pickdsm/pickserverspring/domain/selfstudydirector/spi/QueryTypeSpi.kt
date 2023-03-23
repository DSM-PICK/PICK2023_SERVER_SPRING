package com.pickdsm.pickserverspring.domain.selfstudydirector.spi

import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.Type
import java.time.LocalDate
import java.util.UUID

interface QueryTypeSpi {

    fun queryTypeListByDate(startDate: LocalDate): List<Type>

    fun queryTypeById(typeId: UUID): Type

    fun queryTypeByToday(): Type?

    fun queryDirectorTypeByDate(date: LocalDate): DirectorType?

    fun queryTypeByDate(date: LocalDate): Type?
}
