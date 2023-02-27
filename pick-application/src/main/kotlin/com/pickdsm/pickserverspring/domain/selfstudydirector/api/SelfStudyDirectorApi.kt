package com.pickdsm.pickserverspring.domain.selfstudydirector.api

import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.requst.DomainChangeSelfStudyDirectorRequest
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyListResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyStateResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.TodaySelfStudyTeacherResponse

interface SelfStudyDirectorApi {

    fun getSelfStudyTeacher(month: String): SelfStudyListResponse

    fun getTodaySelfStudyTeacher(): TodaySelfStudyTeacherResponse

    fun getSelfStudyState(): SelfStudyStateResponse

    fun blockMoveClassroom()

    fun changeSelfStudyDirector(request: DomainChangeSelfStudyDirectorRequest)
}
