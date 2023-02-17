package com.pickdsm.pickserverspring.domain.afterschool.spi

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.vo.AfterSchoolRoomVO
import java.util.UUID

interface QueryAfterSchoolSpi {

    fun queryAfterSchoolClassroomListByFloor(floor: Int): List<AfterSchoolRoomVO>

    fun findByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID): AfterSchool?
}
