package com.pickdsm.pickserverspring.domain.teacher.presentation

import com.pickdsm.pickserverspring.domain.teacher.api.TeacherApi
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest.*
import com.pickdsm.pickserverspring.domain.teacher.presentation.dto.request.UpdateStudentStatusRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RequestMapping("/teachers")
@RestController
class TeacherWebAdapter(
    private val teacherApi: TeacherApi,
) {

    @PostMapping("/status")
    fun updateStudentStatus(@RequestBody @Valid request: UpdateStudentStatusRequest) {
        val domainRequest = request.userList.map {
            DomainUpdateStudentStatusElement(userId = it.userId, status = it.status)
        }
        teacherApi.updateStudentStatus(
            DomainUpdateStudentStatusRequest(
                period = request.period,
                userList = domainRequest
            )
        )
    }
}
