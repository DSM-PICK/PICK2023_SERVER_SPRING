package com.pickdsm.pickserverspring.domain.club

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Club(

    val id: UUID = UUID.randomUUID(),

    val studentId: UUID,

    val clubInfoId: UUID,
) {

    fun changeClubStudent(clubId: UUID, studentId: UUID, clubInfoId: UUID): Club {
        return copy(
            id = clubId,
            studentId = studentId,
            clubInfoId = clubInfoId,
        )
    }
}
