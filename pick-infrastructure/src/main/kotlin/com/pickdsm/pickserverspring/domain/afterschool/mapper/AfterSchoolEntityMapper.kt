package com.pickdsm.pickserverspring.domain.afterschool.mapper

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.AfterSchoolEntity

interface AfterSchoolEntityMapper {

    fun domainToEntity(afterSchool: AfterSchool): AfterSchoolEntity

    fun entityToDomain(afterSchoolEntity: AfterSchoolEntity): AfterSchool
}
