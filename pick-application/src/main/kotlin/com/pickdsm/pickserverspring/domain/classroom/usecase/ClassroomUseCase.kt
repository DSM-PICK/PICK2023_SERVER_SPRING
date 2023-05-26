package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.classroom.ClassroomType
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryResponsibleClassroomList
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryResponsibleClassroomList.ResponsibleClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.exception.FloorNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.TypeNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QueryTypeSpi
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.DayOfWeek
import java.time.LocalDate

@ReadOnlyUseCase
class ClassroomUseCase(
    private val userSpi: UserSpi,
    private val queryClassroomSpi: QueryClassroomSpi,
    private val querySelfStudyDirectorSpi: QuerySelfStudyDirectorSpi,
    private val queryClubSpi: QueryClubSpi,
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
    private val queryTypeSpi: QueryTypeSpi,
) : ClassroomApi {

    companion object {
        const val MON_AFTER_SCHOOL_NAME = "자습(월)"
        const val WEN_AFTER_SCHOOL_NAME = "자습(수)"
    }

    override fun queryClassroomList(floor: Int, type: ClassroomType): QueryClassroomList {
        val classrooms = mutableListOf<ClassroomElement>()

        when (type.name) {
            ClassroomType.AFTER_SCHOOL.name -> {
                val afterSchoolList = when (LocalDate.now().dayOfWeek) {
                    DayOfWeek.MONDAY -> {
                        queryAfterSchoolSpi.queryAfterSchoolClassroomByAfterSchoolName(MON_AFTER_SCHOOL_NAME)
                    }

                    DayOfWeek.WEDNESDAY -> {
                        queryAfterSchoolSpi.queryAfterSchoolClassroomByAfterSchoolName(WEN_AFTER_SCHOOL_NAME)
                    }

                    else -> queryAfterSchoolSpi.queryAllAfterSchoolClassroom()
                }

                afterSchoolList.map { afterSchool ->
                    val afterSchoolRoom = ClassroomElement(
                        classroomId = afterSchool.classroomId,
                        typeId = afterSchool.afterSchoolInfoId,
                        name = afterSchool.name,
                        description = afterSchool.description,
                    )
                    classrooms.add(afterSchoolRoom)
                }
            }

            ClassroomType.TUE_CLUB.name, ClassroomType.FRI_CLUB.name -> {
                val clubRoomList = queryClubSpi.queryClubClassroomListByFloor(floor)
                clubRoomList.map {
                    val clubRooms = ClassroomElement(
                        classroomId = it.classroomId,
                        typeId = it.clubId,
                        name = it.name,
                        description = it.description,
                    )
                    classrooms.add(clubRooms)
                }
            }

            ClassroomType.ALL.name, ClassroomType.SELF_STUDY.name -> {
                val allClassroomList = queryClassroomSpi.queryClassroomListByFloorAndByType(floor, type.name)
                allClassroomList.map {
                    val allRooms = ClassroomElement(
                        classroomId = it.id,
                        typeId = it.id,
                        name = it.name,
                        description = "",
                    )
                    classrooms.add(allRooms)
                }
            }

            else -> throw TypeNotFoundException
        }

        return QueryClassroomList(classrooms)
    }

    override fun queryResponsibleClassroomList(): QueryResponsibleClassroomList {
        val teacherId = userSpi.getCurrentUserId()
        val typeId = queryTypeSpi.queryTypeIdByDate(LocalDate.now())
            ?: throw TypeNotFoundException
        val floor = querySelfStudyDirectorSpi.queryResponsibleFloorByTeacherIdAndTypeId(
            teacherId = teacherId,
            typeId = typeId,
        ) ?: throw FloorNotFoundException
        val todayType = queryTypeSpi.queryDirectorTypeByDate(LocalDate.now())
            ?: DirectorType.SELF_STUDY
        val classrooms = mutableListOf<ResponsibleClassroomElement>()

        when (todayType.name) {
            ClassroomType.AFTER_SCHOOL.name -> {
                val afterSchoolList = queryAfterSchoolSpi.queryAfterSchoolClassroomListByFloor(floor)
                afterSchoolList.map {
                    val afterSchoolRooms = ResponsibleClassroomElement(
                        id = it.classroomId,
                        name = it.name,
                        description = it.description,
                    )
                    classrooms.add(afterSchoolRooms)
                }
            }

            ClassroomType.TUE_CLUB.name, ClassroomType.FRI_CLUB.name -> {
                val clubRoomList = queryClubSpi.queryClubClassroomListByFloor(floor)
                clubRoomList.map {
                    val clubRooms = ResponsibleClassroomElement(
                        id = it.classroomId,
                        name = it.name,
                        description = it.description,
                    )
                    classrooms.add(clubRooms)
                }
            }

            ClassroomType.SELF_STUDY.name -> {
                val selfStudyClassroomList = queryClassroomSpi.queryClassroomListByFloorAndByType(floor, todayType.name)
                selfStudyClassroomList.map {
                    val selfStudyRooms = ResponsibleClassroomElement(
                        id = it.id,
                        name = it.name,
                        description = "",
                    )
                    classrooms.add(selfStudyRooms)
                }
            }

            else -> throw TypeNotFoundException
        }

        return QueryResponsibleClassroomList(
            floor = floor,
            responsibleClassroomList = classrooms.sortedBy { it.name },
        )
    }
}
