package com.pickdsm.pickserverspring.domain.selfstudydirector.presentation

import com.pickdsm.pickserverspring.domain.selfstudydirector.api.SelfStudyDirectorApi
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyListResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.TodaySelfStudyTeacherResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RequestMapping("/self-study")
@RestController
class SelfStudyDirectorWebAdapter(
    private val selfStudyDirectorApi: SelfStudyDirectorApi,
) {

    @GetMapping("/director")
    fun getSelfStudyDirector(@RequestParam("month") month: String): SelfStudyListResponse {
        return selfStudyDirectorApi.getSelfStudyTeacher(month)
    }

    @GetMapping("/today")
    fun getTodaySelfStudyDirector(): TodaySelfStudyTeacherResponse =
        selfStudyDirectorApi.getTodaySelfStudyTeacher()
}
