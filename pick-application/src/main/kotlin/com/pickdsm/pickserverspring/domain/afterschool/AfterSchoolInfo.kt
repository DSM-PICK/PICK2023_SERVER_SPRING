package com.pickdsm.pickserverspring.domain.afterschool

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class AfterSchoolInfo(

    val id: UUID = UUID.randomUUID(),

    val afterSchoolName: String,

    val teacherId: UUID,

    val classroomId: UUID,
)
