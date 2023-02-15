package com.pickdsm.pickserverspring.domain.classroom.presentation

import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/class-room")
@RestController
class ClassroomWebAdapter(
    private val classroomApi: ClassroomApi,
) {

    @GetMapping
    fun getClassroomList(
        @RequestParam floor: Int,
        @RequestParam type: String,
    ): QueryClassroomList {
        return classroomApi.queryClassroomList(floor, type)
    }
}
