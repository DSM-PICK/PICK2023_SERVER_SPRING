package com.pickdsm.pickserverspring.domain.club.spi

import com.pickdsm.pickserverspring.domain.club.Club
import java.util.UUID
import com.pickdsm.pickserverspring.domain.club.vo.ClubRoomVO

interface QueryClubSpi {

    fun queryClubClassroomListByFloor(floor: Int): List<ClubRoomVO>

    fun queryClubByClubId(clubId: UUID): Club?
}
