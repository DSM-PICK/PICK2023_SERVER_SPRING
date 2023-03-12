package com.pickdsm.pickserverspring.domain.club.persistence.vo

import com.pickdsm.pickserverspring.domain.club.vo.ClubRoomVO
import com.querydsl.core.annotations.QueryProjection
import java.util.UUID

class QueryClubRoomVO @QueryProjection constructor(
    classroomId: UUID,
    clubId: UUID,
    name: String,
    description: String,
) : ClubRoomVO(
    classroomId = classroomId,
    clubId = clubId,
    name = name,
    description = description,
)
