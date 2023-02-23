package com.pickdsm.pickserverspring.domain.selfstudydirector

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.time.LocalDate
import java.util.UUID

@Aggregate
data class Type(

    val id: UUID = UUID.randomUUID(),

    val date: LocalDate = LocalDate.now(),

    val type: DirectorType,
)
