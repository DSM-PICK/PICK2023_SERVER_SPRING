package com.pickdsm.pickserverspring.domain.club.spi

import com.pickdsm.pickserverspring.domain.club.Club
import java.util.UUID
import com.pickdsm.pickserverspring.domain.club.vo.ClubRoomVO

interface QueryClubSpi {

    fun queryClubClassroomListByFloor(floor: Int): List<ClubRoomVO>

    fun queryClubByClubId(clubId: UUID): Club?

    fun queryClubListByClassroomId(classroomId: UUID): List<Club>

    fun queryStudentIdListByClubId(clubId: UUID): List<UUID>

    fun queryClubStudentIdListByFloor(floor: Int?): List<UUID>

    fun queryClubIdByStudentId(studentId: UUID): UUID
    
    fun queryClubListByClubId(clubId: UUID): List<Club>
}
