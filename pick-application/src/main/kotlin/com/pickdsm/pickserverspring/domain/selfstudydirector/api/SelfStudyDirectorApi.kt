package com.pickdsm.pickserverspring.domain.selfstudydirector.api

import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyListResponse

interface SelfStudyDirectorApi {

    fun getSelfStudyTeacher(month: String): SelfStudyListResponse
}