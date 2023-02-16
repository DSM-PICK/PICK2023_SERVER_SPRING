package com.pickdsm.pickserverspring.domain.club.spi

import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement

interface QueryClubSpi {

    fun queryClubClassroomListByFloor(floor: Int): List<ClassroomElement>
}
