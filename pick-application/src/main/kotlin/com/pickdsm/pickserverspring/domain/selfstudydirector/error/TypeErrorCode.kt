package com.pickdsm.pickserverspring.domain.selfstudydirector.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class TypeErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    TYPE_NOT_FOUND(404, "Type Not Found"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
