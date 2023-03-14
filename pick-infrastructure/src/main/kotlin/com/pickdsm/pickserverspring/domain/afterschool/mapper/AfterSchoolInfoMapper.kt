package com.pickdsm.pickserverspring.domain.afterschool.mapper

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.AfterSchoolInfo
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.AfterSchoolEntity
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.AfterSchoolInfoEntity

interface AfterSchoolInfoMapper {

    fun domainToEntity(afterSchoolInfo: AfterSchoolInfo): AfterSchoolInfoEntity

    fun entityToDomain(afterSchoolInfoEntity: AfterSchoolInfoEntity): AfterSchoolInfo
}
