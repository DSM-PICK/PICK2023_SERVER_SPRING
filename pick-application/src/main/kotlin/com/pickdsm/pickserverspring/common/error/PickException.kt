package com.pickdsm.pickserverspring.common.error

abstract class PickException(
    val errorProperty: ErrorProperty
) : RuntimeException() {

    override fun fillInStackTrace() = this
}
