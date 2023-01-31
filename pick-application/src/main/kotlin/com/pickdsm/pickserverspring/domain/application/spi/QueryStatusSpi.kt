package com.pickdsm.pickserverspring.domain.application.spi

import java.util.UUID

interface QueryStatusSpi {

    fun queryStatusIdList(): List<UUID>
}
