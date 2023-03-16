package com.pickdsm.pickserverspring.domain.teacher.api

import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainComebackStudentRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryMovementStudentList
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryStudentStatusCountResponse
import java.util.UUID

interface TeacherApi {

    fun updateStudentStatus(request: DomainUpdateStudentStatusRequest)

    fun getStudentStatusCount(): QueryStudentStatusCountResponse

    fun comebackStudent(request: DomainComebackStudentRequest)

    fun getMovementStudents(classroomId: UUID): QueryMovementStudentList
}
