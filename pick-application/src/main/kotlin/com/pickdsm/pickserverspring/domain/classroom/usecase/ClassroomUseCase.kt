package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.classroom.ClassroomType
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import com.pickdsm.pickserverspring.domain.classroom.exception.FloorNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.TypeNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QueryTypeSpi
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi

@ReadOnlyUseCase
class ClassroomUseCase(
    private val userSpi: UserSpi,
    private val queryClassroomSpi: QueryClassroomSpi,
    private val querySelfStudyDirectorSpi: QuerySelfStudyDirectorSpi,
    private val queryClubSpi: QueryClubSpi,
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
    private val queryTypeSpi: QueryTypeSpi,
) : ClassroomApi {

    override fun queryClassroomList(floor: Int, type: ClassroomType): QueryClassroomList {
        val classrooms = mutableListOf<ClassroomElement>()

        when (type.name) {
            ClassroomType.AFTER_SCHOOL.name -> {
                val afterSchoolRoomList = queryAfterSchoolSpi.queryAfterSchoolClassroomListByFloor(floor)
                afterSchoolRoomList.map {
                    val afterSchoolRooms = ClassroomElement(
                        id = it.classroomId,
                        name = it.name,
                        description = it.description,
                    )
                    classrooms.add(afterSchoolRooms)
                }
            }

            ClassroomType.CLUB.name -> {
                val clubRoomList = queryClubSpi.queryClubClassroomListByFloor(floor)
                clubRoomList.map {
                    val clubRooms = ClassroomElement(
                        id = it.classroomId,
                        name = it.name,
                        description = it.description,
                    )
                    classrooms.add(clubRooms)
                }
            }

            ClassroomType.ALL.name -> {
                val allClassroomList = queryClassroomSpi.queryClassroomListByFloorAndByType(floor, type.name)
                allClassroomList.map {
                    val allRooms = ClassroomElement(
                        id = it.id,
                        name = it.name,
                        description = "",
                    )
                    classrooms.add(allRooms)
                }
            }

            ClassroomType.SELF_STUDY.name -> {
                val selfStudyClassroomList = queryClassroomSpi.queryClassroomListByFloorAndByType(floor, type.name)
                selfStudyClassroomList.map {
                    val selfStudyRooms = ClassroomElement(
                        id = it.id,
                        name = it.name,
                        description = "",
                    )
                    classrooms.add(selfStudyRooms)
                }
            }

            else -> throw TypeNotFoundException
        }

        return QueryClassroomList(classrooms)
    }

    override fun queryResponsibleClassroomList(): QueryClassroomList {
        val teacherId = userSpi.getCurrentUserId()
        val floor = querySelfStudyDirectorSpi.queryResponsibleFloorByTeacherId(teacherId)
            ?: throw FloorNotFoundException
        val todayType = queryTypeSpi.queryTypeByToday()
        val classrooms = mutableListOf<ClassroomElement>()

        when (todayType?.type?.name) {
            ClassroomType.AFTER_SCHOOL.name -> {
                val afterSchoolList = queryAfterSchoolSpi.queryAfterSchoolClassroomListByFloor(floor)
                afterSchoolList.map {
                    val afterSchoolRooms = ClassroomElement(
                        id = it.classroomId,
                        name = it.name,
                        description = it.description,
                    )
                    classrooms.add(afterSchoolRooms)
                }
            }

            ClassroomType.CLUB.name -> {
                val clubRoomList = queryClubSpi.queryClubClassroomListByFloor(floor)
                clubRoomList.map {
                    val clubRooms = ClassroomElement(
                        id = it.classroomId,
                        name = it.name,
                        description = it.description,
                    )
                    classrooms.add(clubRooms)
                }
            }

            ClassroomType.SELF_STUDY.name -> {
                val selfStudyClassroomList = queryClassroomSpi.queryClassroomListByFloorAndByType(floor, todayType.type.name)
                selfStudyClassroomList.map {
                    val selfStudyRooms = ClassroomElement(
                        id = it.id,
                        name = it.name,
                        description = "",
                    )
                    classrooms.add(selfStudyRooms)
                }
            }

            else -> throw TypeNotFoundException
        }

        return QueryClassroomList(classrooms)
    }
}
