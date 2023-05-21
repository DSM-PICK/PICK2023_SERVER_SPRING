package com.pickdsm.pickserverspring.domain.club.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryClubStudentList
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryClubStudentList.StudentElement
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.ClubInfo
import com.pickdsm.pickserverspring.domain.club.api.ClubApi
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubStudentRequest
import com.pickdsm.pickserverspring.domain.club.exception.ClubInfoNotFoundException
import com.pickdsm.pickserverspring.domain.club.exception.ClubNotFoundException
import com.pickdsm.pickserverspring.domain.club.spi.CommandClubSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.util.UUID

@UseCase
class ClubUseCase(
    private val commandClubSpi: CommandClubSpi,
    private val queryClubSpi: QueryClubSpi,
    private val userSpi: UserSpi,
    private val queryClassroomSpi: QueryClassroomSpi,
) : ClubApi {

    override fun changeClubHead(request: DomainChangeClubHeadRequest) {
        val clubInfo = getClubInfoByClubInfoId(request.clubId)
        commandClubSpi.saveClubInfo(clubInfo.changeClubHead(headId = request.studentId))
    }

    override fun changeClubStudent(request: DomainChangeClubStudentRequest) {
        val oldClub = queryClubSpi.queryClubByStudentId(request.studentId)
        val newClub = queryClubSpi.queryClubByClubId(request.clubId)
            ?: throw ClubNotFoundException

        if (oldClub != null) {
            commandClubSpi.deleteClub(oldClub)
        }

        commandClubSpi.saveClub(
            newClub.changeClubStudent(
                studentId = request.studentId,
                clubInfoId = newClub.clubInfoId,
            ),
        )
    }

    override fun getClubStudentList(clubInfoId: UUID): QueryClubStudentList {
        val clubInfo = getClubInfoByClubInfoId(clubInfoId)
        val clubStudentIdList = queryClubSpi.queryClubStudentIdListByClubInfoId(clubInfoId)
        val clubClassroom = queryClassroomSpi.queryClassroomById(clubInfo.classroomId)
            ?: throw ClassroomNotFoundException

        val teacherInfo = userSpi.queryUserInfoByUserId(clubInfo.teacherId)
        val allStudentInfoList = userSpi.getAllUserInfo()
        val clubStudentInfos = allStudentInfoList.filteringInClubStudentIds(clubStudentIdList)

        val clubStudentList = clubStudentIdList
            .map { clubStudentId ->
                val user = clubStudentInfos.find { user -> clubStudentId == user.id }

                StudentElement(
                    studentId = user?.id,
                    headStatus = clubInfo.headId == user?.id,
                    studentNumber = "${user?.grade}${user?.classNum}${user?.num.toString().padStart(2, '0')}",
                    studentName = user?.name,
                )
            }.sortedBy(StudentElement::studentNumber)

        return QueryClubStudentList(
            clubId = clubInfo.id,
            teacherName = teacherInfo.name,
            classroomName = clubClassroom.name,
            clubName = clubInfo.name,
            studentList = clubStudentList,
        )
    }

    private fun List<User>.filteringInClubStudentIds(clubStudentIdList: List<UUID>): List<User> {
        return this.filter { student ->
            clubStudentIdList.find { clubStudentId -> clubStudentId == student.id } == student.id
        }
    }

    private fun getClubInfoByClubInfoId(clubInfoId: UUID): ClubInfo =
        queryClubSpi.queryClubInfoByClubInfoId(clubInfoId)
            ?: throw ClubInfoNotFoundException
}
