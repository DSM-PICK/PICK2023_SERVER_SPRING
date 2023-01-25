package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomSpi
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ClassPersistenceAdapter(
    private val classroomRepository: ClassroomRepository,
): ClassroomSpi {

    override fun queryClassroomById(classroomId: UUID): Classroom {
        return classroomRepository.findClassroomEntityById(classroomId) ?: throw ClassroomNotFoundException
    }
}