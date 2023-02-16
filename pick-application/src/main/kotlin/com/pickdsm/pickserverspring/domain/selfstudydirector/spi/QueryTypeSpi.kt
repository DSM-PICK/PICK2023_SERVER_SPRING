package com.pickdsm.pickserverspring.domain.selfstudydirector.spi

import com.pickdsm.pickserverspring.domain.selfstudydirector.Type
import java.util.UUID

interface QueryTypeSpi {

    fun queryTypeListByToday(): List<Type>

    fun queryTypeById(typeId: UUID): Type

    fun queryTypeByToday(): Type?
}
