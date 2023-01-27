package com.pickdsm.pickserverspring.domain.application.api

import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList

interface ApplicationApi {

    fun saveApplicationToGoOut(request: DomainApplicationGoOutRequest)

    fun getPicnicApplicationListByGradeAndClassNum(grade: Int?, classNum: Int?): QueryPicnicApplicationList
}
