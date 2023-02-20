package com.pickdsm.pickserverspring.domain.club.api

import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubHeadRequest

interface ClubApi {

    fun changeClubHead(request: DomainChangeClubHeadRequest)
}
