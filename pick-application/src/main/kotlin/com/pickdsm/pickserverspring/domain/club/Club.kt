package com.pickdsm.pickserverspring.domain.club

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
class Club(

    val id: UUID = UUID.randomUUID(),

    val name: String,

    val headId: UUID,

    val teacherId: UUID,

    val studentId: UUID,

    val classroomId: UUID,
) {
    fun changeClubHead(headId: UUID) =
        Club(
            id = this.id,
            name = this.name,
            headId = headId,
            teacherId = this.teacherId,
            studentId = this.studentId,
            classroomId = this.classroomId,
        )

    fun changeClubStudent(clubId: UUID) =
        Club(
            id = this.id,
            name = this.name,
            headId = this.headId,
            teacherId = this.teacherId,
            studentId = this.studentId,
            classroomId = clubId,
        )
}
