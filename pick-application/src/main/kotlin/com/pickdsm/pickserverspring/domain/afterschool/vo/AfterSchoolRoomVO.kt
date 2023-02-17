package com.pickdsm.pickserverspring.domain.afterschool.vo

import java.util.UUID

open class AfterSchoolRoomVO(
    val classroomId: UUID,
    val name: String,
    val description: String,
)
