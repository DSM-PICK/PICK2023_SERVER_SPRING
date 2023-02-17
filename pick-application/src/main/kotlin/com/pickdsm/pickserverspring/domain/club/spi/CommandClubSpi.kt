package com.pickdsm.pickserverspring.domain.club.spi

import java.util.UUID

interface CommandClubSpi {

    fun changeClubHead(clubId: UUID, newHeadStudentId: UUID)
}
