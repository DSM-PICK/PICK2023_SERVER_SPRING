package com.pickdsm.pickserverspring.domain.club

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Club(

    val id: UUID = UUID.randomUUID(),

    val name: String,

    val headId: UUID,

    val teacherId: UUID,

    val studentId: UUID,

    val classroomId: UUID,
) {
    fun changeClubHead(headId: UUID): Club {
        return copy(
            headId = headId,
        )
    }

    fun changeClubStudent(clubId: UUID, studentId: UUID): Club {
        return copy(
            id = clubId,
            studentId = studentId
        )
    }
}
