package com.pickdsm.pickserverspring.domain.application.api

import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest

interface ApplicationApi {

    fun saveApplicationToGoOut(request: DomainApplicationGoOutRequest)
}