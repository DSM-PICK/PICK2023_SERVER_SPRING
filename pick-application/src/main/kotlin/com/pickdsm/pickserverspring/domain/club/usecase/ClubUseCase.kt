package com.pickdsm.pickserverspring.domain.club.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryClubStudentList
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryClubStudentList.StudentElement
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.api.ClubApi
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubHeadRequest
import com.pickdsm.pickserverspring.domain.club.api.dto.DomainChangeClubStudentRequest
import com.pickdsm.pickserverspring.domain.club.exception.ClubNotFoundException
import com.pickdsm.pickserverspring.domain.club.spi.CommandClubSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.teacher.exception.TeacherNotFoundException
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.util.*

@UseCase
class ClubUseCase(
    private val commandClubSpi: CommandClubSpi,
    private val queryClubSpi: QueryClubSpi,
    private val userSpi: UserSpi,
    private val queryClassroomSpi: QueryClassroomSpi,
) : ClubApi {

    override fun changeClubHead(request: DomainChangeClubHeadRequest) {
        val club = queryClubSpi.queryClubInfoByClubId(request.clubId)
            ?: throw ClubNotFoundException

        commandClubSpi.saveClubInfo(
            club.changeClubHead(headId = request.studentId),
        )
    }

    override fun changeClubStudent(request: DomainChangeClubStudentRequest) {
        val club = queryClubSpi.queryClubByClubId(request.clubId)
            ?: throw ClubNotFoundException
        val oldClub = queryClubSpi.queryClubByStudentId(request.studentId)

        if (oldClub != null) {
            commandClubSpi.deleteClub(oldClub)
        }

        commandClubSpi.saveClub(
            club.changeClubStudent(
                clubId = request.clubId,
                studentId = request.studentId,
                clubInfoId = club.clubInfoId,
            ),
        )
    }

    override fun getClubStudentList(clubId: UUID): QueryClubStudentList {
        val clubInfo = queryClubSpi.queryClubInfoByClubId(clubId) ?: throw ClubNotFoundException
        val clubList = queryClubSpi.queryClubListByClubId(clubId)

        val clubClassroom = queryClassroomSpi.queryClassroomById(clubInfo.classroomId)

        val teacherInfo = userSpi.queryUserInfo(listOf(clubInfo.teacherId)).firstOrNull()
            ?: throw TeacherNotFoundException

        val studentIdList = clubList.map { it.studentId }
        val studentInfoList = userSpi.queryUserInfo(studentIdList)

        val clubStudent = clubList.map {
            val user = studentInfoList.find { user -> it.studentId == user.id } ?: throw UserNotFoundException

            val headStatus = clubInfo.headId == user.id

            StudentElement(
                studentId = user.id,
                headStatus = headStatus,
                studentNumber = "${user.grade}${user.classNum}${user.num.toString().padStart(2, '0')}",
                studentName = user.name,
            )
        }

        return QueryClubStudentList(
            clubId = clubInfo.id,
            teacherName = teacherInfo.name,
            classroomName = clubClassroom.name,
            clubName = clubInfo.name,
            studentList = clubStudent,
        )
    }
}
