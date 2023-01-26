package com.pickdsm.pickserverspring.domain.application.api

import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList

interface QueryPicnicApplicationListApi {

    fun getPicnicApplicationList(grade: Int?, classNum: Int?): QueryPicnicApplicationList
}
