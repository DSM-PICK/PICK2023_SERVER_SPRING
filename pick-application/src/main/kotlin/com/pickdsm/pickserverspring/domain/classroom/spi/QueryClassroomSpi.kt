package com.pickdsm.pickserverspring.domain.classroom.spi

<<<<<<< Updated upstream
import com.pickdsm.pickserverspring.domain.classroom.Classroom
import java.util.UUID

interface QueryClassroomSpi {
    fun queryClassroomById(classroomId: UUID): Classroom
}
=======
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement

interface QueryClassroomSpi {

    fun queryClassroomList(floor: Int): List<ClassroomElement>
}
>>>>>>> Stashed changes
