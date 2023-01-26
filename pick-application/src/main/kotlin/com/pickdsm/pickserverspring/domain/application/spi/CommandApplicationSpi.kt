package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Application

interface CommandApplicationSpi {

    fun saveApplication(application: Application)
}