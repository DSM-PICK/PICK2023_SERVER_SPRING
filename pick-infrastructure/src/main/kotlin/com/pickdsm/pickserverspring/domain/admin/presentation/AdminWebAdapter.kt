package com.pickdsm.pickserverspring.domain.admin.presentation

import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.ChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.DeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.AfterSchoolApi
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainCreateAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainDeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.presentation.dto.requset.CreateAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.club.api.ClubApi
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubHeadRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{after-school-id}")
    fun createAfterSchoolStudent(
        @PathVariable("after-school-id")
        afterSchoolId: UUID,
        @RequestBody
        @Valid
        request: CreateAfterSchoolStudentRequest,
    ) {
        val domainRequest = DomainCreateAfterSchoolStudentRequest(
            afterSchoolId = afterSchoolId,
            studentIds = request.userIdList,
        )
        afterSchoolApi.createAfterSchoolStudent(domainRequest)
    }
}
