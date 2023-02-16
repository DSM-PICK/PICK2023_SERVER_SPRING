package com.pickdsm.pickserverspring.domain.club.spi

import com.pickdsm.pickserverspring.domain.club.Club

interface QueryClubSpi {

    fun queryClubList(): List<Club>
}
