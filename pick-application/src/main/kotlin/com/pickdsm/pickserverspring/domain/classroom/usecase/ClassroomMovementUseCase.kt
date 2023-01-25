package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomMovementApi
import com.pickdsm.pickserverspring.domain.classroom.spi.CommandClassroomMovementSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.util.*

@UseCase
class ClassroomMovementUseCase(
    private val queryClassroomSpi: QueryClassroomSpi,
    private val userSpi: UserSpi,
    private val commandClassroomMovementSpi: CommandClassroomMovementSpi
) : ClassroomMovementApi {

    override fun saveClassroomMovement(classroomId: UUID) {
        val classroom = queryClassroomSpi.queryClassroomById(classroomId)
        val studentId = userSpi.getCurrentUserId()

        commandClassroomMovementSpi.saveClassroom(studentId , classroom)
    }
}