package com.pickdsm.pickserverspring.domain.afterschool.spi

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.AfterSchoolInfo
import com.pickdsm.pickserverspring.domain.afterschool.vo.AfterSchoolRoomVO
import java.util.UUID

interface QueryAfterSchoolSpi {

    fun queryAfterSchoolClassroomListByFloor(floor: Int): List<AfterSchoolRoomVO>

    fun queryAfterSchoolListByClassroomId(classroomId: UUID): List<AfterSchool>

    fun findByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID): AfterSchool?

    fun findByAfterSchoolId(afterSchoolId: UUID): AfterSchool?

    fun queryAfterSchoolInfoByAfterSchoolId(afterSchoolId: UUID): AfterSchoolInfo?

    fun queryAfterSchoolStudentIdByFloor(floor: Int?): List<UUID>

    fun queryAfterSchoolListByAfterSchoolId(afterSchoolId: UUID): List<AfterSchool>

    fun queryAfterSchoolIdByStudentId(studentId: UUID): UUID?

    fun existsAfterSchoolByStudentIds(afterSchoolStudentIds: List<UUID>): Boolean

    fun queryAfterSchoolClassroomIdByStudentId(studentId: UUID): UUID?
}
