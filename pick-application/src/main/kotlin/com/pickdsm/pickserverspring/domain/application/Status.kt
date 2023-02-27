package com.pickdsm.pickserverspring.domain.application

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.time.LocalDate
import java.util.UUID

@Aggregate
data class Status(

    val id: UUID = UUID.randomUUID(),

    val studentId: UUID,

    val teacherId: UUID,

    val date: LocalDate = LocalDate.now(),

    val startPeriod: Int,

    val endPeriod: Int,

    val type: StatusType,
) {
    fun changeStatusToAttendance(teacherId: UUID, endPeriod: Int): Status {
        return copy(
            teacherId = teacherId,
            endPeriod = endPeriod,
            type = StatusType.ATTENDANCE,
        )
    }

    fun changeStatusOfClass(teacherId: UUID, startPeriod: Int, endPeriod: Int, type: StatusType): Status {
        return copy(
            teacherId = teacherId,
            startPeriod = startPeriod,
            endPeriod = endPeriod,
            type = type,
        )
    }

    fun changePicnicStatus(studentId: UUID, teacherId: UUID, type: StatusType): Status {
        return copy(
            studentId = studentId,
            teacherId = teacherId,
            type = type,
        )
    }
}
