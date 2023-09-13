package com.pickdsm.pickserverspring.domain.classroom.usecase

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.classroom.ClassroomType
import com.pickdsm.pickserverspring.domain.classroom.api.ClassroomApi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryClassroomList.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryResponsibleClassroomList
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.QueryResponsibleClassroomList.ResponsibleClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.exception.FloorNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomMovementSpi
import com.pickdsm.pickserverspring.domain.classroom.spi.QueryClassroomSpi
import com.pickdsm.pickserverspring.domain.club.spi.QueryClubSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.TypeNotFoundException
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.QueryTypeSpi
import com.pickdsm.pickserverspring.domain.user.dto.UserInfo
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
    private val queryClassroomMovementSpi: QueryClassroomMovementSpi,
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

                afterSchoolList.map {
                    val afterSchoolRoom = ClassroomElement(
                        classroomId = it.classroomId,
                        typeId = it.afterSchoolInfoId,
                        name = it.name,
                        description = it.description,
                    )
                    classrooms.add(afterSchoolRoom)
                }
            }

            ClassroomType.TUE_CLUB.name, ClassroomType.FRI_CLUB.name -> {
                val clubRoomList = queryClubSpi.queryClubClassroomListByFloor(floor)

                clubRoomList.map {
                    val clubRooms = ClassroomElement(
                        classroomId = it.classroomId,
                        typeId = it.clubInfoId,
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
        val typeId = queryTypeSpi.queryTypeIdByDate(LocalDate.now()) ?: throw TypeNotFoundException
        val floor = querySelfStudyDirectorSpi.queryResponsibleFloorByTeacherIdAndTypeId(
            teacherId = teacherId,
            typeId = typeId,
        ) ?: throw FloorNotFoundException
        val todayType = queryTypeSpi.queryDirectorTypeByDate(LocalDate.now()) ?: DirectorType.SELF_STUDY
        val classrooms = mutableListOf<ResponsibleClassroomElement>()
        var isUserExist: Boolean

        when (todayType.name) {
            ClassroomType.AFTER_SCHOOL.name -> {
                val afterSchoolList = queryAfterSchoolSpi.queryAfterSchoolClassroomListByFloor(floor)

                afterSchoolList.map { afterSchool ->
                    val afterSchoolUserList =
                        queryAfterSchoolSpi.queryAfterSchoolListByClassroomId(afterSchool.classroomId)
                    print(afterSchoolUserList)
                    val afterSchoolMovementUserList =
                        queryClassroomMovementSpi.queryClassroomMovementListByClassroomId(afterSchool.classroomId)

                    isUserExist = !(afterSchoolUserList.isEmpty() && afterSchoolMovementUserList.isEmpty())

                    val afterSchoolRooms = ResponsibleClassroomElement(
                        id = afterSchool.classroomId,
                        name = afterSchool.name,
                        description = afterSchool.description,
                        isUserExist = isUserExist,
                    )
                    classrooms.add(afterSchoolRooms)
                }
            }

            ClassroomType.TUE_CLUB.name, ClassroomType.FRI_CLUB.name -> {
                val clubRoomList = queryClubSpi.queryClubClassroomListByFloor(floor)

                clubRoomList.map {
                    val clubUserList = queryClubSpi.queryClubStudentIdListByClubInfoId(it.clubInfoId)
                    val clubMovementUserList =
                        queryClassroomMovementSpi.queryClassroomMovementListByClassroomId(it.classroomId)

                    isUserExist = !(clubUserList.isEmpty() && clubMovementUserList.isEmpty())

                    val clubRooms = ResponsibleClassroomElement(
                        id = it.classroomId,
                        name = it.name,
                        description = it.description,
                        isUserExist = isUserExist,
                    )
                    classrooms.add(clubRooms)
                }
            }

            ClassroomType.SELF_STUDY.name -> {
                val selfStudyClassroomList = queryClassroomSpi.queryClassroomListByFloorAndByType(floor, todayType.name)

                selfStudyClassroomList.map {
                    val classroom = queryClassroomSpi.queryClassroomById(it.id) ?: throw ClassroomNotFoundException
                    val classroomMovementUsers =
                        queryClassroomMovementSpi.queryClassroomMovementListByClassroomId(classroom.id)
                    var classroomUserList = emptyList<UserInfo>()
                    val isClassroom = !(classroom.grade == null && classroom.classNum == null)

                    if (isClassroom) {
                        classroomUserList = userSpi.queryUserInfoByGradeAndClassNum(
                            grade = classroom.grade,
                            classNum = classroom.classNum,
                        )
                    }

                    isUserExist = !(classroomMovementUsers.isEmpty() && classroomUserList.isEmpty())

                    val selfStudyRooms = ResponsibleClassroomElement(
                        id = it.id,
                        name = it.name,
                        description = "",
                        isUserExist = isUserExist,
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
