package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Application
import java.util.UUID

interface CommandApplicationSpi {

    fun saveApplication(application: Application)

    fun changePermission(applicationIdList: List<UUID>)
}
