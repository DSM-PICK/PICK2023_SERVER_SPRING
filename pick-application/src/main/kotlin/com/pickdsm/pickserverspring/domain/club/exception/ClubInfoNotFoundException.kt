package com.pickdsm.pickserverspring.domain.club.exception

import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.domain.club.error.ClubErrorCode

object ClubInfoNotFoundException : PickException(
    ClubErrorCode.CLUB_INFO_NOT_FOUND,
)
