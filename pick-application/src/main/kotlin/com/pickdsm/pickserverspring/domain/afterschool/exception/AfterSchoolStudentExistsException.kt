package com.pickdsm.pickserverspring.domain.afterschool.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.afterschool.error.AfterSchoolErrorCode

object AfterSchoolStudentExistsException : PickException(
    AfterSchoolErrorCode.AFTER_SCHOOL_STUDENT_EXISTS,
)
