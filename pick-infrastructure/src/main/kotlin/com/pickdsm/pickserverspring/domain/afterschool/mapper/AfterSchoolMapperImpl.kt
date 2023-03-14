package com.pickdsm.pickserverspring.domain.afterschool.mapper

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolNotFoundException
import com.pickdsm.pickserverspring.domain.afterschool.persistence.AfterSchoolInfoRepository
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.AfterSchoolEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class AfterSchoolMapperImpl(
    private val afterSchoolInfoRepository: AfterSchoolInfoRepository
) : AfterSchoolMapper {

    override fun domainToEntity(afterSchool: AfterSchool): AfterSchoolEntity {
        val afterSchoolInfoEntity = afterSchoolInfoRepository.findByIdOrNull(afterSchool.afterSchoolInfoId)
            ?: throw AfterSchoolNotFoundException

        return AfterSchoolEntity(
            id = afterSchool.id,
            studentId = afterSchool.studentId,
            afterSchoolInfoEntity = afterSchoolInfoEntity
        )
    }

    override fun entityToDomain(afterSchoolEntity: AfterSchoolEntity): AfterSchool {
        return AfterSchool(
            id = afterSchoolEntity.id,
            studentId = afterSchoolEntity.studentId,
            afterSchoolInfoId = afterSchoolEntity.afterSchoolInfoEntity.id
        )
    }
}
