package com.pickdsm.pickserverspring.domain.selfstudydirector.presentation

import com.pickdsm.pickserverspring.domain.selfstudydirector.api.SelfStudyDirectorApi
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.TodaySelfStudyTeacherResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/self-study")
@RestController
class SelfStudyDirectorWebAdapter(
    private val selfStudyDirectorApi: SelfStudyDirectorApi,
) {

    @GetMapping("/today")
    fun getTodaySelfStudyDirector(): TodaySelfStudyTeacherResponse =
        selfStudyDirectorApi.getTodaySelfStudyTeacher()
}
