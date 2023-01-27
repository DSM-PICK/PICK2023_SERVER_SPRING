package com.pickdsm.pickserverspring.domain.classroom

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.*

@Aggregate
class ClassroomMovement(

    val id: UUID = UUID(0, 0),

    val studentId: UUID,

    val classroomId: UUID,
)
