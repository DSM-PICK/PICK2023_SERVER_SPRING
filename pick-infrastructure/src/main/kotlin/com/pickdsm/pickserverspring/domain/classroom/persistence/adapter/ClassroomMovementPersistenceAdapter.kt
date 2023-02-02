package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.mapper.ClassroomMapper
import com.pickdsm.pickserverspring.domain.classroom.mapper.ClassroomMovementMapper
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomMovementRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomMovementEntity
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomMovementSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.*

@Adapter
class ClassroomMovementPersistenceAdapter(
    private val classroomMovementRepository: ClassroomMovementRepository,
    private val classroomMapper: ClassroomMapper,
) : ClassroomMovementSpi {

    override fun saveClassroom(studentId: UUID, classroom: Classroom) {
        val classroomEntity = classroomMapper.domainToEntity(classroom)
        val classroomMovementEntity = ClassroomMovementEntity(
            id = UUID.randomUUID(),
            studentId = studentId,
            classroomEntity = classroomEntity,
        )

        classroomMovementRepository.save(classroomMovementEntity)
    }
}
