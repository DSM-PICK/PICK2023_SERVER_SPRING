package com.pickdsm.pickserverspring.domain.application.persistence.vo

import com.pickdsm.pickserverspring.domain.application.vo.PicnicApplicationVO
import com.querydsl.core.annotations.QueryProjection
import java.util.UUID

class QueryPicnicApplicationVO @QueryProjection constructor(
    studentId: UUID,
    startPeriod: Int,
    endPeriod: Int,
    reason: String,
) : PicnicApplicationVO(
    studentId = studentId,
    startPeriod = startPeriod,
    endPeriod = endPeriod,
    reason = reason,
)
