package com.pickdsm.pickserverspring.domain.afterschool.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class AfterSchoolErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    AFTER_SCHOOL_NOT_FOUND(404, "AfterSchool Not Found"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
