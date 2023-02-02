package com.pickdsm.pickserverspring.domain.application.presentation

import com.pickdsm.pickserverspring.domain.application.api.ApplicationApi
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.request.DomainApplicationUserIdsRequest
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicStudentList
import com.pickdsm.pickserverspring.domain.application.presentation.dto.request.ApplicationGoOutRequest
import com.pickdsm.pickserverspring.domain.application.presentation.dto.request.ApplicationUserIdsRequest
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomMovementApi
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
import org.springframework.web.bind.annotation.PathVariable
import java.util.*
import javax.validation.Valid

@RequestMapping("/applications")
@RestController
class ApplicationWebAdapter(
    private val classroomMovementApi: ClassroomMovementApi,
    private val applicationApi: ApplicationApi,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{classroom-id}")
    fun saveClassMovement(@PathVariable("classroom-id") classRoomId: UUID) {
        classroomMovementApi.saveClassroomMovement(classRoomId)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun saveApplicationToGoOut(
        @RequestBody
        @Valid
        request: ApplicationGoOutRequest,
    ) {
        val domainRequest = DomainApplicationGoOutRequest(
            startTime = request.startTime,
            endTime = request.endTime,
            reason = request.reason,
        )
        applicationApi.saveApplicationToGoOut(domainRequest)
    }

    @GetMapping
    fun queryPicnicStudentListByToday(): QueryPicnicStudentList {
        return applicationApi.queryPicnicStudentListByToday()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping
    fun permitPicnicApplication(
        @RequestBody
        request: ApplicationUserIdsRequest,
    ) {
        val domainRequest = DomainApplicationUserIdsRequest(
            userIdList = request.userIdList,
        )
        applicationApi.permitPicnicApplication(domainRequest)
    }
}
