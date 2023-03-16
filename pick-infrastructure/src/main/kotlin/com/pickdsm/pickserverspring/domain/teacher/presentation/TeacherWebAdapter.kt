package com.pickdsm.pickserverspring.domain.teacher.presentation

import com.pickdsm.pickserverspring.domain.application.api.ApplicationApi
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainPicnicAcceptOrRefuseRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryStudentStatusList
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryResponsibleClassroomList
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.teacher.api.TeacherApi
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainComebackStudentRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.request.DomainUpdateStudentStatusRequest
import com.pickdsm.pickserverspring.domain.teacher.api.dto.response.QueryMovementStudentList
import com.pickdsm.pickserverspring.domain.teacher.presentation.dto.request.ComebackStudentRequest
import com.pickdsm.pickserverspring.domain.teacher.presentation.dto.request.PicnicAcceptOrRefuseRequest
import com.pickdsm.pickserverspring.domain.teacher.presentation.dto.request.UpdateStudentStatusRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RequestMapping("/teachers")
@RestController
class TeacherWebAdapter(
    private val teacherApi: TeacherApi,
    private val applicationApi: ApplicationApi,
    private val classroomApi: ClassroomApi,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/status")
    fun updateStudentStatus(
        @RequestBody
        @Valid
        request: UpdateStudentStatusRequest,
    ) {
        val domainRequest = DomainUpdateStudentStatusRequest(
            period = request.period,
            userId = request.userId,
            status = request.status,
        )
        teacherApi.updateStudentStatus(domainRequest)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping
    fun comebackStudent(
        @RequestBody
        @Valid
        request: ComebackStudentRequest,
    ) {
        val domainRequest = DomainComebackStudentRequest(
            studentId = request.studentId,
            endPeriod = request.endPeriod,
        )
        teacherApi.comebackStudent(domainRequest)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/status")
    fun picnicAcceptOrRefuse(
        @RequestBody
        @Valid
        request: PicnicAcceptOrRefuseRequest,
    ) {
        val domainRequest = DomainPicnicAcceptOrRefuseRequest(
            type = request.type,
            userIdList = request.userIdList,
        )
        applicationApi.savePicnicAcceptOrRefuse(domainRequest)
    }

    @GetMapping
    fun queryPicnicApplicationListByGradeAndClassNum(
        @RequestParam grade: String?,
        @RequestParam classNum: String?,
        @RequestParam floor: Int?,
        @RequestParam type: DirectorType,
    ): QueryPicnicApplicationList {
        return applicationApi.queryPicnicApplicationListByGradeAndClassNum(grade, classNum, floor, type)
    }

    @GetMapping("/responsible")
    fun queryResponsibleFloor(): QueryResponsibleClassroomList {
        return classroomApi.queryResponsibleClassroomList()
    }

    @GetMapping("/students/{classroom-id}")
    fun queryStudentStatusByToday(
        @PathVariable("classroom-id") classroomId: UUID,
    ): QueryStudentStatusList {
        return applicationApi.getAllStudentStatusByClassroomId(classroomId)
    }

    @GetMapping("/{classroom-id}")
    fun getMovementStatus(
        @PathVariable("classroom-id") classroomId: UUID
    ): QueryMovementStudentList {
        return teacherApi.getMovementStudents(classroomId)
    }
}
