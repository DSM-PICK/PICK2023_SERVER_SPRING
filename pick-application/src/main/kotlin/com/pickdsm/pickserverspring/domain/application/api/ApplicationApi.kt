package com.pickdsm.pickserverspring.domain.application.api

import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicStudentList

interface ApplicationApi {

    fun saveApplicationToGoOut(request: DomainApplicationGoOutRequest)

    fun queryPicnicApplicationListByGradeAndClassNum(grade: String, classNum: String): QueryPicnicApplicationList

    fun queryPicnicStudentListByToday(): QueryPicnicStudentList
}
