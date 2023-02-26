package com.pickdsm.pickserverspring.domain.time.spi

import com.pickdsm.pickserverspring.domain.time.Time

interface QueryTimeSpi {

    fun queryNowPeriod(time: Time): Int
}
