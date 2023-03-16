package com.pickdsm.pickserverspring.domain.classroom.api

import com.pickdsm.pickserverspring.domain.classroom.api.dto.request.DomainClassroomMovementRequest
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomMovementLocationResponse
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomMovementStudentList
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryMovementStudentList
import java.util.UUID

interface ClassroomMovementApi {

    fun saveClassroomMovement(request: DomainClassroomMovementRequest)

    fun queryMovementStudentList(grade: Int?, classNum: Int?, floor: Int?): QueryMovementStudentList

    fun returnClassroomMovement()

    fun getClassroomMovementLocation(): QueryClassroomMovementLocationResponse

    fun getClassroomMovementStudentList(classroomId: UUID): QueryClassroomMovementStudentList
}
