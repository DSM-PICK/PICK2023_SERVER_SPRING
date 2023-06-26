package com.pickdsm.pickserverspring.domain.application

import com.pickdsm.pickserverspring.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Application(

    val id: UUID = UUID.randomUUID(),

    val reason: String,

    val statusId: UUID,

    val isReturn: Boolean,
) {
    fun changeStatusToAttendance(isReturn: Boolean): Application {
        return copy(
            isReturn = isReturn,
        )
    }
}
