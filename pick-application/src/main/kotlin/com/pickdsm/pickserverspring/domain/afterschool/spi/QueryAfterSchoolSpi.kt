package com.pickdsm.pickserverspring.domain.afterschool.spi

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import java.util.UUID

interface QueryAfterSchoolSpi {

    fun queryAfterSchoolClassroomListByFloor(floor: Int): List<ClassroomElement>

    fun findByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID): AfterSchool?
}
