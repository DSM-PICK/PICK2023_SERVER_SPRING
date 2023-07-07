package com.pickdsm.pickserverspring.domain.teacher.api

import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainComebackStudentRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryMovementStudentList
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryMyBuckGradeAndClassNumResponse
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryStudentStatusCountResponse
import java.util.UUID

interface TeacherApi {

    fun updateStudentStatus(request: DomainUpdateStudentStatusRequest)

    fun queryStudentStatusCount(): QueryStudentStatusCountResponse

    fun comebackStudent(request: DomainComebackStudentRequest)

    fun queryMovementStudents(classroomId: UUID): QueryMovementStudentList

    fun queryMyBuckGradeAndClassNum(): QueryMyBuckGradeAndClassNumResponse
}
