package com.pickdsm.pickserverspring.domain.selfstudydirector

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.time.LocalDate
import java.util.*

@Aggregate
class SelfStudyDirector(

    val id: UUID,

    val floor: Int,

    val teacherId: UUID,

    val date: LocalDate,

    val type: DirectorType,
)
