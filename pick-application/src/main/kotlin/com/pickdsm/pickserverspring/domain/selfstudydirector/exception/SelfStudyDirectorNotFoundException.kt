package com.pickdsm.pickserverspring.domain.selfstudydirector.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.selfstudydirector.error.SelfStudyDirectorErrorCode

object SelfStudyDirectorNotFoundException : PickException(
    SelfStudyDirectorErrorCode.SELF_STUDY_DIRECTOR_NOT_FOUND,
)