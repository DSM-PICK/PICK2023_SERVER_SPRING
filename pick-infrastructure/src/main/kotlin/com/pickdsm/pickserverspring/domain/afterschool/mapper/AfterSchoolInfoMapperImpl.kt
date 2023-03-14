package com.pickdsm.pickserverspring.domain.afterschool.mapper

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchoolInfo
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.AfterSchoolInfoEntity
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class AfterSchoolInfoMapperImpl(
    private val classroomRepository: ClassroomRepository,
) : AfterSchoolInfoMapper {
    override fun domainToEntity(afterSchoolInfo: AfterSchoolInfo): AfterSchoolInfoEntity {
        val classroomEntity = classroomRepository.findByIdOrNull(afterSchoolInfo.classroomId)
            ?: throw ClassroomNotFoundException

        return AfterSchoolInfoEntity(
            id = afterSchoolInfo.id,
            afterSchoolName = afterSchoolInfo.afterSchoolName,
            teacherId = afterSchoolInfo.teacherId,
            classroomEntity = classroomEntity
        )
    }

    override fun entityToDomain(afterSchoolInfoEntity: AfterSchoolInfoEntity): AfterSchoolInfo {
        return AfterSchoolInfo(
            id = afterSchoolInfoEntity.id,
            afterSchoolName = afterSchoolInfoEntity.afterSchoolName,
            teacherId = afterSchoolInfoEntity.teacherId,
            classroomId = afterSchoolInfoEntity.classroomEntity.id
        )
    }
}
