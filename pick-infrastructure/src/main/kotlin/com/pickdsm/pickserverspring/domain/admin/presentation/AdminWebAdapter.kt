package com.pickdsm.pickserverspring.domain.admin.presentation

import com.pickdsm.pickserverspring.domain.admin.api.AdminApi
import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest
import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest.DomainUpdateStudentElement
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentAttendanceList
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryTypeResponse
import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.ChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.ChangeSelfStudyDirectorRequset
import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.DeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.PicnicPassRequest
import com.pickdsm.pickserverspring.domain.admin.presentation.dto.request.UpdateStudentStatusOfClassRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.AfterSchoolApi
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.request.DomainCreateAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.request.DomainDeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.response.QueryAfterSchoolStudentList
import com.pickdsm.pickserverspring.domain.afterschool.presentation.dto.requset.CreateAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.application.api.ApplicationApi
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainPicnicPassRequest
import com.pickdsm.pickserverspring.domain.club.api.ClubApi
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubStudentRequest
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.SelfStudyDirectorApi
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.requst.DomainChangeSelfStudyDirectorRequest
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyListResponse
import com.pickdsm.pickserverspring.domain.selfstudydirector.api.dto.response.SelfStudyStateResponse
import com.pickdsm.pickserverspring.domain.teacher.api.TeacherApi
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryStudentStatusCountResponse
import org.springframework.format.annotation.DateTimeFormat
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
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.util.UUID
import javax.validation.Valid

@RequestMapping("/admin")
@RestController
class AdminWebAdapter(
    private val adminApi: AdminApi,
    private val afterSchoolApi: AfterSchoolApi,
    private val applicationApi: ApplicationApi,
    private val clubApi: ClubApi,
    private val selfStudyDirectorApi: SelfStudyDirectorApi,
    private val teacherApi: TeacherApi,
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/class")
    fun updateStudentStatusOfClass(
        @RequestBody
        @Valid
        request: UpdateStudentStatusOfClassRequest,
    ) {
        val domainRequest = DomainUpdateStudentStatusOfClassRequest(
            userList = request.userList.map {
                DomainUpdateStudentElement(
                    userId = it.userId,
                    status = it.status,
                )
            },
        )
        adminApi.updateStudentStatusOfClass(domainRequest)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping
    fun blockMoveClassroom() {
        selfStudyDirectorApi.blockMoveClassroom()
    }

    @PatchMapping("/club")
    fun changeClubStudent(
        @RequestBody
        @Valid
        request: DomainChangeClubStudentRequest,
    ) {
        val domainRequest = DomainChangeClubStudentRequest(
            studentId = request.studentId,
            clubId = request.clubId,
        )
        clubApi.changeClubStudent(domainRequest)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/teacher")
    fun changeSelfStudyDirector(
        @RequestBody
        @Valid
        request: ChangeSelfStudyDirectorRequset,
    ) {
        val domainRequest = DomainChangeSelfStudyDirectorRequest(
            teacherId = request.teacherId,
            floor = request.floor,
            date = request.date,
        )
        selfStudyDirectorApi.changeSelfStudyDirector(domainRequest)
    }

    @GetMapping("/attendance/{classroom-id}")
    fun getStudentAttendanceList(
        @PathVariable("classroom-id")
        classroomId: UUID,
        @RequestParam
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        date: LocalDate,
    ): QueryStudentAttendanceList {
        return adminApi.getStudentAttendanceList(classroomId, date)
    }

    @GetMapping("/state")
    fun getSelfStudyState(): SelfStudyStateResponse {
        return selfStudyDirectorApi.getSelfStudyState()
    }

    @GetMapping
    fun getTypeByToday(
        @RequestParam
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        date: LocalDate,
    ): QueryTypeResponse {
        return adminApi.getTypeByDate(date)
    }

    @GetMapping("/director")
    fun getSelfStudyDirector(@RequestParam("month") month: String): SelfStudyListResponse {
        return selfStudyDirectorApi.getSelfStudyTeacher(month)
    }

    @GetMapping("/students/count")
    fun getStudentStatusCount(): QueryStudentStatusCountResponse {
        return teacherApi.getStudentStatusCount()
    }

    @GetMapping("/afterSchool/{after-school-id}")
    fun getAfterSchoolStudents(
        @PathVariable ("after-school-id")
        afterSchoolId: UUID,
    ) : QueryAfterSchoolStudentList {
        return afterSchoolApi.getAfterSchoolStudents(afterSchoolId)
    }
}
