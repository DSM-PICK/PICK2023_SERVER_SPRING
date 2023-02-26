package com.pickdsm.pickserverspring.domain.selfstudydirector.spi

import com.pickdsm.pickserverspring.domain.selfstudydirector.Type
import java.time.LocalDate
import java.util.UUID
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

interface QueryTypeSpi {

    fun queryTypeListByToday(): List<Type>

    fun queryTypeById(typeId: UUID): Type

    fun queryTypeByToday(date: LocalDate): Type?
}
