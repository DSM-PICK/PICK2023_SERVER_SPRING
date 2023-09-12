package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import java.util.UUID

interface QueryClassroomMovementSpi {
    fun queryClassroomMovementByStatus(status: Status): ClassroomMovement?

    fun existClassroomMovementByStudentId(studentId: UUID): Boolean

    fun queryClassroomMovementByStudentIdAndToday(studentId: UUID): ClassroomMovement?

    fun queryClassroomMovementClassroomIdByStatusId(statusId: UUID): UUID?

    fun queryClassroomMovementListByClassroomId(classroomId: UUID): List<ClassroomMovement>
}
