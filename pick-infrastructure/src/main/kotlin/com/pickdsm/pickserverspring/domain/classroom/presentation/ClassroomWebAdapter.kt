package com.pickdsm.pickserverspring.domain.classroom.presentation

import com.pickdsm.pickserverspring.domain.classroom.ClassroomType
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomApi
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomMovementApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomMovementLocationResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/class-room")
@RestController
class ClassroomWebAdapter(
    private val classroomApi: ClassroomApi,
    private val classroomMovementApi: ClassroomMovementApi,
) {

    @GetMapping
    fun getClassroomList(
        @RequestParam floor: Int,
        @RequestParam type: ClassroomType,
    ): QueryClassroomList {
        return classroomApi.queryClassroomList(floor, type)
    }

    @GetMapping("/location")
    fun getLocationClassroomMovement(): QueryClassroomMovementLocationResponse {
        return classroomMovementApi.getClassroomMovementLocation()
    }
}
