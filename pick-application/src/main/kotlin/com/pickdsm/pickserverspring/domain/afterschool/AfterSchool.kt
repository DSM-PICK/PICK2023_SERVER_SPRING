package com.pickdsm.pickserverspring.domain.afterschool

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class AfterSchool(

    val id: UUID = UUID.randomUUID(),

    val studentId: UUID,

    val afterSchoolInfoId: UUID,
)
