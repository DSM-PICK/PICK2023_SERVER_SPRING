package com.pickdsm.pickserverspring.domain.application.api

import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainPicnicPassRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicStudentList
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryStudentStatusList
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import java.util.*

interface ApplicationApi {

    fun saveApplicationToGoOut(request: DomainApplicationGoOutRequest)

    fun queryPicnicApplicationListByGradeAndClassNum(grade: String?, classNum: String?, floor: Int?, type: DirectorType): QueryPicnicApplicationList

    fun queryPicnicStudentListByToday(): QueryPicnicStudentList

    fun queryAllStudentStatusByClassroomAndType(classroomId: UUID, type: String): QueryStudentStatusList

    fun savePicnicPass(request: DomainPicnicPassRequest)
}
