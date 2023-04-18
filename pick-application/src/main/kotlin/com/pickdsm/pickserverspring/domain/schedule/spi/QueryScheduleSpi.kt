package com.pickdsm.pickserverspring.domain.schedule.spi

interface QueryScheduleSpi {
    fun queryIsHomecomingDay(date: String): Boolean
}