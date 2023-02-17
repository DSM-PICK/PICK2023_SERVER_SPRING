package com.pickdsm.pickserverspring.domain.club.spi

import com.pickdsm.pickserverspring.domain.club.Club
import java.util.UUID

interface QueryClubSpi {

    fun queryClubList(): List<Club>

    fun queryClubByClubId(clubId: UUID): Club?
}
