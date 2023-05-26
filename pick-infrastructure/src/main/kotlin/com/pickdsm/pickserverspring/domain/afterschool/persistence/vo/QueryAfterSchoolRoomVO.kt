package com.pickdsm.pickserverspring.domain.afterschool.persistence.vo

import com.pickdsm.pickserverspring.domain.afterschool.vo.AfterSchoolRoomVO
import com.querydsl.core.annotations.QueryProjection
import java.util.*

class QueryAfterSchoolRoomVO @QueryProjection constructor(
    classroomId: UUID,
    afterSchoolInfoId: UUID,
    name: String,
    description: String,
) : AfterSchoolRoomVO(
    classroomId = classroomId,
    afterSchoolInfoId = afterSchoolInfoId,
    name = name,
    description = description,
)
