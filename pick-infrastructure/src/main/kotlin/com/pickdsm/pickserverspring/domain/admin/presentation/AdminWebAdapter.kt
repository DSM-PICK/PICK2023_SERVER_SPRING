package com.pickdsm.pickserverspring.domain.admin.presentation

import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.ChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.ChangeSelfStudyDirectorRequset
import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.DeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.PicnicPassRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.AfterSchoolApi
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainCreateAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.DomainDeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.presentation.dto.requset.CreateAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.application.api.ApplicationApi
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainPicnicPassRequest
import com.pickdsm.pickserverspring.domain.club.api.ClubApi
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.SelfStudyDirectorApi
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.requst.DomainChangeSelfStudyDirectorRequest
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyStateResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@RequestMapping("/admin")
@RestController
class AdminWebAdapter(
    private val afterSchoolApi: AfterSchoolApi,
    private val clubApi: ClubApi,
    private val selfStudyDirectorApi: SelfStudyDirectorApi,
    private val applicationApi: ApplicationApi,
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

    @GetMapping("/state")
    fun getSelfStudyState(): SelfStudyStateResponse {
        return selfStudyDirectorApi.getSelfStudyState()
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/picnic")
    fun savePassIssued(
        @RequestBody
        @Valid
        request: PicnicPassRequest,
    ) {
        val domainRequest = DomainPicnicPassRequest(
            userIdList = request.userIdList,
            reason = request.reason,
            startPeriod = request.startPeriod,
            endPeriod = request.endPeriod,
        )
        applicationApi.savePicnicPass(domainRequest)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/teacher")
    fun changeSelfStudyDirector(
        @RequestBody
        @Valid
        request: ChangeSelfStudyDirectorRequset,
    ) {
        val domainRequset = DomainChangeSelfStudyDirectorRequest(
            teacherId = request.teacherId,
            floor = request.floor,
            date = request.date,
        )
        selfStudyDirectorApi.changeSelfStudyDirector(domainRequset)
    }
}
