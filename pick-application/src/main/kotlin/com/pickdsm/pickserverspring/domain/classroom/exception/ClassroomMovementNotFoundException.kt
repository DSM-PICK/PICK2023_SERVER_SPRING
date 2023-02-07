package com.pickdsm.pickserverspring.domain.classroom.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.classroom.error.ClassroomErrorCode

object ClassroomMovementNotFoundException : PickException(
    ClassroomErrorCode.CLASSROOM_MOVEMENT_NOT_FOUND,
)
