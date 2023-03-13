package com.pickdsm.pickserverspring.domain.club

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class ClubInfo(

    val id: UUID = UUID.randomUUID(),

    val name: String,

    val headId: UUID,

    val teacherId: UUID,

    val classroomId: UUID,
) {
    fun changeClubHead(headId: UUID): ClubInfo {
        return copy(
            headId = headId,
        )
    }
}
