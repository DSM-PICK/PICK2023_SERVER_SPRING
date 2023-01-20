package com.pickdsm.pickserverspring.domain.classroom

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
class ClassroomMovement(

    val id: UUID,

    val classroomId: UUID,
)
