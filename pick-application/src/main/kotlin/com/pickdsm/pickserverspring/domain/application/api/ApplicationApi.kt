package com.pickdsm.pickserverspring.domain.application.api

import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainPicnicPassRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicStudentList
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryStudentStatusList
import java.util.UUID

interface ApplicationApi {

    fun saveApplicationToGoOut(request: DomainApplicationGoOutRequest)

    fun queryPicnicApplicationListByGradeAndClassNum(grade: String, classNum: String): QueryPicnicApplicationList

    fun queryPicnicStudentListByToday(): QueryPicnicStudentList

    fun queryAllStudentStatusByClassroomAndType(classroomId: UUID, type: String): QueryStudentStatusList

    fun savePicnicPass(request: DomainPicnicPassRequest)
}
