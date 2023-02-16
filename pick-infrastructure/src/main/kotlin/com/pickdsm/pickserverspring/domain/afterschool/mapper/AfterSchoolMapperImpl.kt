package com.pickdsm.pickserverspring.domain.afterschool.mapper

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.AfterSchoolEntity
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class AfterSchoolMapperImpl(
    private val classroomRepository: ClassroomRepository,
) : AfterSchoolMapper {

    override fun domainToEntity(afterSchool: AfterSchool): AfterSchoolEntity {
        val classroomEntity = classroomRepository.findByIdOrNull(afterSchool.classroomId)
            ?: throw ClassroomNotFoundException

        return AfterSchoolEntity(
            id = afterSchool.id,
            afterSchoolName = afterSchool.afterSchoolName,
            teacherId = afterSchool.teacherId,
            studentId = afterSchool.studentId,
            classroomEntity = classroomEntity,
        )
    }

    override fun entityToDomain(afterSchoolEntity: AfterSchoolEntity): AfterSchool {
        return AfterSchool(
            id = afterSchoolEntity.id,
            afterSchoolName = afterSchoolEntity.afterSchoolName,
            teacherId = afterSchoolEntity.teacherId,
            studentId = afterSchoolEntity.studentId,
            classroomId = afterSchoolEntity.classroomEntity.id,
        )
    }
}
