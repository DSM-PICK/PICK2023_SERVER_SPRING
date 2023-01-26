package com.pickdsm.pickserverspring.domain.classroom.presentation

import com.pickdsm.pickserverspring.domain.classroom.api.QueryClassroomListApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/applications")
@RestController
class ClassroomWebAdapter(
    private val classroomApi: QueryClassroomListApi,
) {

    @GetMapping
    fun getClassroomList(@RequestParam floor: Int): QueryClassroomList {
        return classroomApi.queryClassroomList(floor)
    }
}
