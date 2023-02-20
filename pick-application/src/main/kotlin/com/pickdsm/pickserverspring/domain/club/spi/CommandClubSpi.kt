package com.pickdsm.pickserverspring.domain.club.spi

import com.pickdsm.pickserverspring.domain.club.Club

interface CommandClubSpi {

    fun saveClub(club: Club)
}
