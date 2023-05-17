package com.pickdsm.pickserverspring.domain.classroom.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.classroom.error.ClassroomErrorCode

object AfterSchoolCannotMovementException: PickException(
    ClassroomErrorCode.AFTER_SCHOOL_CANNOT_MOVEMENT
)