package com.pickdsm.pickserverspring.domain.selfstudydirector.spi

import com.pickdsm.pickserverspring.domain.selfstudydirector.Type

interface CommandTypeSpi {
    fun saveType(type: Type)
}
