package com.pickdsm.pickserverspring.domain.club.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.club.error.ClubErrorCode

object ClubNotFoundException : PickException(
    ClubErrorCode.CLUB_NOT_FOUND,
)
