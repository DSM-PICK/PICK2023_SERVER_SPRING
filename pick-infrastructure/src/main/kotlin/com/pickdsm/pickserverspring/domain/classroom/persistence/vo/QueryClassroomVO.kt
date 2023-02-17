package com.pickdsm.pickserverspring.domain.classroom.persistence.vo

import com.pickdsm.pickserverspring.domain.classroom.vo.ClassroomVO
import com.querydsl.core.annotations.QueryProjection
import java.util.UUID

class QueryClassroomVO @QueryProjection constructor(
    id: UUID,
    name: String,
) : ClassroomVO(
    id = id,
    name = name,
)
