package com.pickdsm.pickserverspring.domain.club.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.admin.api.dto.response.QueryStudentList
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
import java.util.UUID

@UseCase
class ClubUseCase(
    private val commandClubSpi: CommandClubSpi,
    private val queryClubSpi: QueryClubSpi,
    private val userSpi: UserSpi,
    private val queryClassroomSpi: QueryClassroomSpi,
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

    override fun getClubStudentList(clubId: UUID): QueryStudentList {
        val club = queryClubSpi.queryClubByClubId(clubId)
            ?: throw ClubNotFoundException
        val clubList = queryClubSpi.queryClubListByClubId(clubId)

        val clubClassroom = queryClassroomSpi.queryClassroomById(club.classroomId)

        val teacherId = clubList.map{ it.teacherId }
        val teacherInfo = userSpi.queryUserInfo(teacherId).firstOrNull()
            ?: throw TeacherNotFoundException

        val studentIdList = clubList.map{ it.studentId }
        val studentInfoList = userSpi.queryUserInfo(studentIdList)

        val clubStudent = clubList.map {
            val user = studentInfoList.find { user ->
                it.studentId == user.id
            } ?: throw UserNotFoundException

            QueryStudentList.StudentElement(
                studentId = user.id,
                studentNumber = user.num,
                studentName = user.name,
            )
        }

        return QueryStudentList(
            clubId = club.id,
            teacherName = teacherInfo.name,
            classroomName = clubClassroom.name,
            clubName = club.name,
            studentList = clubStudent,
        )
    }
}
