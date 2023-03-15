package com.pickdsm.pickserverspring.domain.application.presentation

import com.pickdsm.pickserverspring.domain.application.api.ApplicationApi
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryMyPicnicOrMovementResponse
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicStudentList
import com.pickdsm.pickserverspring.domain.application.presentation.dto.request.ApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomMovementApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.request.DomainClassroomMovementRequest
import com.pickdsm.pickserverspring.domain.classroom.presentation.dto.request.ClassroomMovementRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RequestMapping("/applications")
@RestController
class ApplicationWebAdapter(
    private val classroomMovementApi: ClassroomMovementApi,
    private val applicationApi: ApplicationApi,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{classroom-id}")
    fun saveClassMovement(
        @PathVariable("classroom-id")
        classRoomId: UUID,
        @RequestBody
        @Valid
        request: ClassroomMovementRequest,
    ) {
        val domainRequest = DomainClassroomMovementRequest(
            classroomId = classRoomId,
            period = request.period,
        )
        classroomMovementApi.saveClassroomMovement(domainRequest)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun saveApplicationToGoOut(
        @RequestBody
        @Valid
        request: ApplicationGoOutRequest,
    ) {
        val domainRequest = DomainApplicationGoOutRequest(
            desiredStartPeriod = request.desiredStartPeriod,
            desiredEndPeriod = request.desiredEndPeriod,
            reason = request.reason,
        )
        applicationApi.saveApplicationToGoOut(domainRequest)
    }

    @GetMapping
    fun queryPicnicStudentListByToday(): QueryPicnicStudentList {
        return applicationApi.queryPicnicStudentListByToday()
    }

    @GetMapping("/return")
    fun getMyPicnicEndTime(): QueryMyPicnicOrMovementResponse {
        return applicationApi.getMyPicnicEndTime()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun returnClassroom() = classroomMovementApi.returnClassroomMovement()
}
