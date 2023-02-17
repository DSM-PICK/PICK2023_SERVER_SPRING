package com.pickdsm.pickserverspring.domain.club.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.club.api.ClubApi
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.club.spi.CommandClubSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi

@UseCase
class ClubUseCase(
    private val commandClubSpi: CommandClubSpi,
) : ClubApi {

    override fun changeClubHead(request: DomainChangeClubHeadRequest) {
        commandClubSpi.changeClubHead(
            clubId = request.clubId,
            newHeadStudentId = request.studentId,
        )
    }
}
