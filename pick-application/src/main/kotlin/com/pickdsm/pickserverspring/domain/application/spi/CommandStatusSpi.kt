package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Status

interface CommandStatusSpi {
    fun deleteAllMovementStudent(statusList: List<Status>)

    fun deleteStatus(status: Status)
}
