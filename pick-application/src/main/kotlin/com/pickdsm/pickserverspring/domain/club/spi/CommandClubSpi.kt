package com.pickdsm.pickserverspring.domain.club.spi

import com.pickdsm.pickserverspring.domain.club.Club
import com.pickdsm.pickserverspring.domain.club.ClubInfo

interface CommandClubSpi {

    fun saveClub(club: Club)

    fun saveClubInfo(clubInfo: ClubInfo)

    fun deleteClub(club: Club)
}
