package com.pickdsm.pickserverspring.domain.application

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
class Application(

    val id: UUID = UUID.randomUUID(),

    val reason: String,

    val statusId: UUID,
)
