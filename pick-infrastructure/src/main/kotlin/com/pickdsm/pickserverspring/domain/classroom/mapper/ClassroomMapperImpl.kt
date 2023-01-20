package com.pickdsm.pickserverspring.domain.classroom.mapper

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomEntity
import org.springframework.stereotype.Component

@Component
class ClassroomMapperImpl : ClassroomMapper {

    override fun domainToEntity(classroom: Classroom): ClassroomEntity {
        return ClassroomEntity(
            id = classroom.id,
            name = classroom.name,
            floor = classroom.floor
        )
    }

    override fun entityToDomain(classroomEntity: ClassroomEntity): Classroom {
        return Classroom(
            id = classroomEntity.id,
            name = classroomEntity.name,
            floor = classroomEntity.floor
        )
    }
}
