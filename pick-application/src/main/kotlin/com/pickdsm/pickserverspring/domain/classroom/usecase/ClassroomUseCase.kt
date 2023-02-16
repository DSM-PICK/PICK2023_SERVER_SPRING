package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolNotFoundException
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.classroom.ClassroomType
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.exception.FloorNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.exception.ClubNotFoundException
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi

@ReadOnlyUseCase
class ClassroomUseCase(
    private val userSpi: UserSpi,
    private val queryClassroomSpi: QueryClassroomSpi,
    private val querySelfStudyDirectorSpi: QuerySelfStudyDirectorSpi,
    private val queryClubSpi: QueryClubSpi,
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
) : ClassroomApi {

    override fun queryClassroomList(floor: Int, type: ClassroomType): QueryClassroomList {
        val classroomList = queryClassroomSpi.queryClassroomListByFloor(floor)
        val classrooms = mutableListOf<ClassroomElement>()

        when (type.name) {
            ClassroomType.SELF_STUDY.name -> {
                classroomList.map {
                    val classroom = classroomList.find { classroom -> classroom.grade != null }
                        ?: throw ClassroomNotFoundException
                    val classroomElement = ClassroomElement(
                        id = classroom.id,
                        name = classroom.name,
                        description = "", // 교실은 별다른 설명 없음
                    )
                    classrooms.add(classroomElement)
                }
            }

            ClassroomType.CLUB.name -> {
                val clubList = queryClubSpi.queryClubList()
                classroomList.map {
                    val club = clubList.find { club -> club.classroomId == it.id }
                        ?: throw ClubNotFoundException
                    val classroomElement = ClassroomElement(
                        id = it.id,
                        name = it.name,
                        description = club.name,
                    )
                    classrooms.add(classroomElement)
                }
            }

            ClassroomType.AFTER_SCHOOL.name -> {
                val afterSchoolList = queryAfterSchoolSpi.queryAfterSchoolList()
                classroomList.map {
                    val afterSchool = afterSchoolList.find { afterSchool -> afterSchool.classroomId == it.id }
                        ?: throw AfterSchoolNotFoundException
                    val classroomElement = ClassroomElement(
                        id = it.id,
                        name = it.name,
                        description = afterSchool.afterSchoolName,
                    )
                    classrooms.add(classroomElement)
                }
            }

            ClassroomType.ALL.name -> {
                classroomList.map {
                    val classroomElement = ClassroomElement(
                        id = it.id,
                        name = it.name,
                        description = "", // 전체 타입도 별다른 설명 없음
                    )
                    classrooms.add(classroomElement)
                }
            }
        }

        return QueryClassroomList(classrooms)
    }

    override fun queryResponsibleClassroomList(): QueryClassroomList {
        val teacherId = userSpi.getCurrentUserId()

        val floor = querySelfStudyDirectorSpi.queryResponsibleFloorByTeacherId(teacherId)
            ?: throw FloorNotFoundException

        val classroomList = queryClassroomSpi.queryClassroomListByFloor(floor)

        val response = classroomList.map {
            ClassroomElement(
                id = it.id,
                name = it.name,
                description = "",
            )
        }

        // TODO: 층 별 교실 리스트 조회처럼 수정하기

        return QueryClassroomList(response)
    }
}
