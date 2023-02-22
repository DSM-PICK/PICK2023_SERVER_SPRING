package com.pickdsm.pickserverspring.domain.classroom.api

import com.pickdsm.pickserverspring.domain.classroom.api.dto.request.DomainClassroomMovementRequest
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryMovementStudentList
import java.time.LocalDate

interface ClassroomMovementApi {

    fun saveClassroomMovement(request: DomainClassroomMovementRequest)

    fun queryMovementStudentList(grade: Int?, classNum: Int?, floor: Int?, date: LocalDate): QueryMovementStudentList
}
