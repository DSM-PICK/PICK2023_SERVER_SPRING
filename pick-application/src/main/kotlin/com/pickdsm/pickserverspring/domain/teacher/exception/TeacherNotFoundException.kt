package com.pickdsm.pickserverspring.domain.teacher.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.teacher.error.TeacherErrorCode

object TeacherNotFoundException : PickException(
    TeacherErrorCode.TEACHER_NOT_FOUND,
)
