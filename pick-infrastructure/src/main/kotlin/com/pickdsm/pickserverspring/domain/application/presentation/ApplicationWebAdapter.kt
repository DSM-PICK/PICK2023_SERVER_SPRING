package com.pickdsm.pickserverspring.domain.application.presentation

import com.pickdsm.pickserverspring.domain.application.api.QueryPicnicApplicationListApi
import com.pickdsm.pickserverspring.domain.application.api.dto.response.QueryPicnicApplicationList
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomMovementApi
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

@RequestMapping("/applications")
@RestController
class ApplicationWebAdapter(
    private val classroomMovementApi: ClassroomMovementApi,
    private val queryPicnicApplicationListApi: QueryPicnicApplicationListApi,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{classroom-id}")
    fun saveClassMovement(@PathVariable("classroom-id") classRoomId: UUID) {
        classroomMovementApi.saveClassroomMovement(classRoomId)
    }

    @GetMapping
    fun getPicnicApplicationList(
        @RequestParam grade: Int?,
        @RequestParam classNum: Int?,
    ): QueryPicnicApplicationList {
        return queryPicnicApplicationListApi.getPicnicApplicationListByGradeAndClassNum(grade, classNum)
    }
}
