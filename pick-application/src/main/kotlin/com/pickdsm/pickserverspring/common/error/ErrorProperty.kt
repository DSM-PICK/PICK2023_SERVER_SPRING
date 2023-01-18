package com.pickdsm.pickserverspring.common.error

interface ErrorProperty {

    fun status(): Int

    fun message(): String

}