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
            floor = classroom.floor,
            grade = if (classroom.grade == null) null else classroom.grade,
            classNum = if(classroom.classNum == null) null else classroom.classNum,
            homeroomTeacherId = classroom.homeroomTeacherId,
        )
    }

    override fun entityToDomain(classroomEntity: ClassroomEntity): Classroom {
        return Classroom(
            id = classroomEntity.id,
            name = classroomEntity.name,
            floor = classroomEntity.floor,
            grade = classroomEntity.grade,
            classNum = if(classroomEntity.classNum == null) null else classroomEntity.classNum,
            homeroomTeacherId = classroomEntity.homeroomTeacherId!!,
        )
    }
}
