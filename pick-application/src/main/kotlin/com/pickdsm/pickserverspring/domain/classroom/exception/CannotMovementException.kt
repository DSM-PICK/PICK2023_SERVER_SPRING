package com.pickdsm.pickserverspring.domain.classroom.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.classroom.error.ClassroomErrorCode

object CannotMovementException : PickException(
    ClassroomErrorCode.CANNOT_MOVEMENT,
)
