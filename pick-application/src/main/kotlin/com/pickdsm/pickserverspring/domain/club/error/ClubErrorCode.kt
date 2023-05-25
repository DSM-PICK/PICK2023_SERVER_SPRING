package com.pickdsm.pickserverspring.domain.club.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class ClubErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    CLUB_NOT_FOUND(404, "Club Not Found"),
    CLUB_INFO_NOT_FOUND(404, "Club Info Not Found"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
