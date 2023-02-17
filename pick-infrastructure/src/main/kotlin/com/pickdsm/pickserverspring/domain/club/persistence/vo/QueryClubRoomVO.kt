package com.pickdsm.pickserverspring.domain.club.persistence.vo

import com.pickdsm.pickserverspring.domain.club.vo.ClubRoomVO
import com.querydsl.core.annotations.QueryProjection
import java.util.UUID

class QueryClubRoomVO @QueryProjection constructor(
    classroomId: UUID,
    name: String,
    description: String,
) : ClubRoomVO(
    classroomId = classroomId,
    name = name,
    description = description,
)
