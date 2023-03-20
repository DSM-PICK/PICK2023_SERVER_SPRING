package com.pickdsm.pickserverspring.domain.admin.api

import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentAttendanceList
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentListByGradeAndClassNum
import java.util.UUID
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryTypeResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import java.time.LocalDate

interface AdminApi {

    fun updateStudentStatusOfClass(request: DomainUpdateStudentStatusOfClassRequest)

    fun getStudentAttendanceList(classroomId: UUID, date: LocalDate): QueryStudentAttendanceList

    fun getTypeByDate(date: LocalDate): QueryTypeResponse

    fun getStudentStatusListByGradeAndClassNum(grade: Int?, classNum: Int?): QueryStudentListByGradeAndClassNum

    fun saveType(date: LocalDate, type: DirectorType)

    fun updateType(typeId: UUID, date: LocalDate, type: DirectorType)
}
