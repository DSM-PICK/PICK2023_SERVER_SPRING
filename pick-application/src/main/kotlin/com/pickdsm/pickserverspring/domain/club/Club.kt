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
    fun copy(
        id: UUID = this.id,
        name: String = this.name,
        headId: UUID = this.headId,
        teacherId: UUID = this.teacherId,
        studentId: UUID = this.studentId,
        classroomId: UUID = this.classroomId,
    ) = Club(
        id = id,
        name = name,
        headId = headId,
        teacherId = teacherId,
        studentId = studentId,
        classroomId = classroomId,
    )
}
