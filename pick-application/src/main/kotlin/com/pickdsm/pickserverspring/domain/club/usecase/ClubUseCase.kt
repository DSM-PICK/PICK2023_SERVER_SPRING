package com.pickdsm.pickserverspring.domain.club.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.club.api.ClubApi
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubStudentRequest
import com.pickdsm.pickserverspring.domain.club.exception.ClubNotFoundException
import com.pickdsm.pickserverspring.domain.club.spi.CommandClubSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi

@UseCase
class ClubUseCase(
    private val commandClubSpi: CommandClubSpi,
    private val queryClubSpi: QueryClubSpi,
    private val userSpi: UserSpi,
) : ClubApi {

    override fun changeClubHead(request: DomainChangeClubHeadRequest) {
        val club = queryClubSpi.queryClubByClubId(request.clubId)
            ?: throw ClubNotFoundException

        commandClubSpi.saveClub(
            club.changeClubHead(headId = request.studentId),
        )
    }

    override fun changeClubStudent(request: DomainChangeClubStudentRequest) {
        val club = queryClubSpi.queryClubByClubId(request.clubId)
            ?: throw ClubNotFoundException

        commandClubSpi.saveClub(
            club.changeClubStudent(clubId = request.clubId, studentId = request.studentId, classroomId = club.classroomId),
        )
    }
}
