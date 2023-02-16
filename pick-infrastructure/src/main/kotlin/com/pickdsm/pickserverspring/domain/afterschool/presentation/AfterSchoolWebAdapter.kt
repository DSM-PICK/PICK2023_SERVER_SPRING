package com.pickdsm.pickserverspring.domain.afterschool.presentation

import com.pickdsm.pickserverspring.domain.afterschool.api.AfterSchoolApi
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainDeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.presentation.dto.requset.DeleteAfterSchoolStudentRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RequestMapping("/admin")
@RestController
class AfterSchoolWebAdapter(
    private val afterSchoolApi: AfterSchoolApi,
) {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/")
    fun deleteAfterSchoolStudent(
        @RequestBody
        @Valid
        requset: DeleteAfterSchoolStudentRequest,
    ) {
        val domainRequset = DomainDeleteAfterSchoolStudentRequest(
            afterSchoolId = requset.afterSchoolId,
            studentId = requset.studentId,
        )
        afterSchoolApi.deleteAfterSchoolStudent(domainRequset)
    }
}
