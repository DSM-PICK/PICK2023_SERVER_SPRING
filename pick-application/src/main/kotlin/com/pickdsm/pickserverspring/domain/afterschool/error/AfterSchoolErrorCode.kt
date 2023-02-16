package com.pickdsm.pickserverspring.domain.afterschool.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class AfterSchoolErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    AFTER_SCHOOL_NOT_FOUND(404, "After School Not Found"),
    AFTER_SCHOOL_STUDENT_NOT_FOUND(404, "After School Student Not Found"), ;

    override fun status() = status
    override fun message() = message
}
