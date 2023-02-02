package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Status

interface QueryStatusSpi {

    fun getAllPicnicStatus(): List<Status>
}
