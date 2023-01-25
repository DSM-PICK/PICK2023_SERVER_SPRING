package com.pickdsm.pickserverspring.domain.application.presentation

import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomMovementApi
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RequestMapping("/applications")
@RestController
class ApplicationWebAdapter(
    private val classroomMovementApi: ClassroomMovementApi,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{classroom-id}")
    fun saveClassMovement(@PathVariable("classroom-id") classRoomId: UUID) {
        classroomMovementApi.saveClassroomMovement(classRoomId)
    }
}
