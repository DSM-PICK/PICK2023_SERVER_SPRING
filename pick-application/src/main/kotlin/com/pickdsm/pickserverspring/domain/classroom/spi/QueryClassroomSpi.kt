package com.pickdsm.pickserverspring.domain.classroom.spi

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.vo.ClassroomVO
import java.util.UUID

interface QueryClassroomSpi {

    fun queryClassroomById(classroomId: UUID): Classroom?

    fun queryClassroomListByFloorAndByType(floor: Int, classroomType: String): List<ClassroomVO>

    fun queryClassroomGradeByFloor(floor: Int): Int?

    fun queryClassroomByGradeAndClassNum(grade: Int?, classNum: Int?): Classroom?

    fun queryClassroomIdByGradeAndClassNum(grade: Int, classNum: Int): UUID?

    fun queryClassroomNameByClassroomId(classroomId: UUID): String?
}
