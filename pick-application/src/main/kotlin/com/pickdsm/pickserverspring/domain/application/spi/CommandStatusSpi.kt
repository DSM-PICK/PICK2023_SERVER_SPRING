package com.pickdsm.pickserverspring.domain.application.spi

import java.util.UUID

interface CommandStatusSpi {

    fun changeStatusToPicnic(statusIdList: List<UUID>)
}
