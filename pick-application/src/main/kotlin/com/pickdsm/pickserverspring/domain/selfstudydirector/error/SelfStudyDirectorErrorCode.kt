package com.pickdsm.pickserverspring.domain.selfstudydirector.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class SelfStudyDirectorErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    SELF_STUDY_DIRECTOR_NOT_FOUND(404, "Self Study Not Found"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
