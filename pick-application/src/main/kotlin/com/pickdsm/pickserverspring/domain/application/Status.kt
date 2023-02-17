package com.pickdsm.pickserverspring.domain.application

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.time.LocalDate
import java.util.UUID

@Aggregate
class Status(

    val id: UUID = UUID.randomUUID(),

    val studentId: UUID,

    val teacherId: UUID,

    val date: LocalDate = LocalDate.now(),

    val startPeriod: Int,

    val endPeriod: Int,

    val type: StatusType,
) {
    fun changeStatusToAttendance(teacherId: UUID, endPeriod: Int) =
        Status(
            id = this.id,
            studentId = this.studentId,
            teacherId = teacherId,
            date = this.date,
            startPeriod = this.startPeriod,
            endPeriod = endPeriod,
            type = StatusType.ATTENDANCE,
        )
}
