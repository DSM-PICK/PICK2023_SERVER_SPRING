package com.pickdsm.pickserverspring.domain.classroom.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.classroom.error.ClassroomErrorCode

object ClassroomMovementStudentNotFoundException : PickException(
    ClassroomErrorCode.CLASSROOM_MOVEMENT_STUDENT_NOT_FOUND
)
