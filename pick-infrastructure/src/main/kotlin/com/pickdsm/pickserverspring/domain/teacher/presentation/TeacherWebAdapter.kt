package com.pickdsm.pickserverspring.domain.teacher.presentation

import com.pickdsm.pickserverspring.domain.application.api.ApplicationApi
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.teacher.api.TeacherApi
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest.DomainUpdateStudentStatusElement
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryStudentStatusCountResponse
import com.pickdsm.pickserverspring.domain.teacher.presentation.dto.request.UpdateStudentStatusRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import javax.validation.Valid

@RequestMapping("/teachers")
@RestController
class TeacherWebAdapter(
    private val teacherApi: TeacherApi,
    private val applicationApi: ApplicationApi,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/status")
    fun updateStudentStatus(
        @RequestBody
        @Valid
        request: UpdateStudentStatusRequest,
    ) {
        val domainRequest = request.userList.map {
            DomainUpdateStudentStatusElement(userId = it.userId, status = it.status)
        }
        teacherApi.updateStudentStatus(
            DomainUpdateStudentStatusRequest(
                period = request.period,
                userList = domainRequest,
            ),
        )
    }

    @GetMapping("/students/count")
    fun getStudentStatusCount(): QueryStudentStatusCountResponse {
        return teacherApi.getStudentStatusCount()
    }
    
    @GetMapping
    fun queryPicnicApplicationListByGradeAndClassNum(
        @RequestParam grade: String,
        @RequestParam classNum: String,
    ): QueryPicnicApplicationList {
        return applicationApi.queryPicnicApplicationListByGradeAndClassNum(grade, classNum)
    }
}
