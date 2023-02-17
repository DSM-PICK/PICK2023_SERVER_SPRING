package com.pickdsm.pickserverspring.domain.admin.presentation

import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.ChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.DeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.AfterSchoolApi
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainDeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.club.api.ClubApi
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubHeadRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/admin")
@RestController
class AdminWebAdapter(
    private val afterSchoolApi: AfterSchoolApi,
    private val clubApi: ClubApi,
) {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun deleteAfterSchoolStudent(
        @RequestBody
        @Valid
        request: DeleteAfterSchoolStudentRequest,
    ) {
        val domainRequest = DomainDeleteAfterSchoolStudentRequest(
            afterSchoolId = request.afterSchoolId,
            studentId = request.studentId,
        )
        afterSchoolApi.deleteAfterSchoolStudent(domainRequest)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/head")
    fun changeClubHead(
        @RequestBody
        @Valid
        request: ChangeClubHeadRequest,
    ) {
        val domainRequest = DomainChangeClubHeadRequest(
            clubId = request.clubId,
            studentId = request.studentId,
        )
        clubApi.changeClubHead(domainRequest)
    }
}
