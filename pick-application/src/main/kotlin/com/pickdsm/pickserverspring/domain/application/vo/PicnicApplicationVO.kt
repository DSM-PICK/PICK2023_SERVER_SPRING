package com.pickdsm.pickserverspring.domain.application.vo

import java.util.UUID

open class PicnicApplicationVO(
    val studentId: UUID,
    val startPeriod: Int,
    val endPeriod: Int,
    val reason: String,
)
