package com.pickdsm.pickserverspring.domain.club.api.dto

import java.util.*

data class DomainChangeClubStudentRequest(
    val studentId: UUID,
    val classroomId: UUID,
)
