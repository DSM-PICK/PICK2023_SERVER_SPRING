package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import java.util.*

@Adapter
class ClassPersistenceAdapter(
    private val classroomRepository: ClassroomRepository,
) : ClassroomSpi {

    override fun queryClassroomById(classroomId: UUID): Classroom =
        classroomRepository.findClassroomEntityById(classroomId) ?: throw ClassroomNotFoundException

    override fun queryClassroomListByFloor(floor: Int): List<ClassroomElement> {
        val classroomList = classroomRepository.findClassroomEntityByFloor(floor)

        return classroomList
            .map {
                classroom -> ClassroomElement(
                    id = classroom.id,
                    name = classroom.name,
                )
            }
    }

}
