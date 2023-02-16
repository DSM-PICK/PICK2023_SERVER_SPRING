package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.classroom.ClassroomMovement
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomMovementApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.request.DomainClassroomMovementRequest
import com.pickdsm.pickserverspring.domain.classroom.spi.CommandClassroomMovementSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.TimeQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.time.exception.TimeNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate

@UseCase
class ClassroomMovementUseCase(
    private val queryClassroomSpi: QueryClassroomSpi,
    private val userSpi: UserSpi,
    private val commandClassroomMovementSpi: CommandClassroomMovementSpi,
    private val timeQueryTeacherSpi: TimeQueryTeacherSpi,
    private val statusCommandTeacherSpi: StatusCommandTeacherSpi,
) : ClassroomMovementApi {

    override fun saveClassroomMovement(request: DomainClassroomMovementRequest) {
        val classroom = queryClassroomSpi.queryClassroomById(request.classroomId)
        val studentId = userSpi.getCurrentUserId()
        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())
        val time = timeList.timeList.find { time -> time.period == request.period }
            ?: throw TimeNotFoundException

        val status = Status(
            studentId = studentId,
            teacherId = studentId, // TODO: 해당 층 자습감독쌤 아이디 넣기
            startPeriod = time.period,
            endPeriod = time.period,
            type = StatusType.MOVEMENT,
        )
        val saveStatusId = statusCommandTeacherSpi.saveStatusAndGetStatusId(status)
        commandClassroomMovementSpi.saveClassroomMovement(
            ClassroomMovement(
                classroomId = classroom.id,
                statusId = saveStatusId,
            ),
        )
    }
}
