package com.pickdsm.pickserverspring.domain.club.api

import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryClubStudentList
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubStudentRequest
import java.util.UUID

interface ClubApi {

    fun changeClubHead(request: DomainChangeClubHeadRequest)

    fun changeClubStudent(request: DomainChangeClubStudentRequest)

    fun getClubStudentList(clubId: UUID): QueryClubStudentList
}
