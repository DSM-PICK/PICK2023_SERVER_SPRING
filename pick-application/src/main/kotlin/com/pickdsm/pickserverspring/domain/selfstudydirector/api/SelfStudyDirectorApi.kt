package com.pickdsm.pickserverspring.domain.selfstudydirector.api

import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyListResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.TodaySelfStudyTeacherResponse

interface SelfStudyDirectorApi {

    fun getSelfStudyTeacher(month: String): SelfStudyListResponse

    fun getTodaySelfStudyTeacher(): TodaySelfStudyTeacherResponse
}
