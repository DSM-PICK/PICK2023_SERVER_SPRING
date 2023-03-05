package com.pickdsm.pickserverspring.domain.teacher.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class TeacherErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    TEACHER_NOT_FOUND(404, "Teacher Not Found"), ;

    override fun status(): Int = status
    override fun message(): String = message
}