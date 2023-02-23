package com.pickdsm.pickserverspring.domain.club.api.dto

import java.util.UUID

data class DomainChangeClubStudentRequest(
    val clubId: UUID,
    val studentId: UUID,
)
