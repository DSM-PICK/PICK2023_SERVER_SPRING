package com.pickdsm.pickserverspring.domain.club.api

import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubStudentRequest

interface ClubApi {

    fun changeClubHead(request: DomainChangeClubHeadRequest)

    fun changeClubStudent(request: DomainChangeClubStudentRequest)
}
