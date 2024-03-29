package com.pickdsm.pickserverspring.domain.classroom.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.classroom.error.ClassroomErrorCode

object CannotMovementWeekendException : PickException(
    ClassroomErrorCode.CANNOT_MOVEMENT_WEEKEND,
)
