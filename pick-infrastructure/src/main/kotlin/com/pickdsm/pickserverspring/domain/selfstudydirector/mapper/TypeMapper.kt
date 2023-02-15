package com.pickdsm.pickserverspring.domain.selfstudydirector.mapper

import com.pickdsm.pickserverspring.domain.selfstudydirector.Type
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.TypeEntity

interface TypeMapper {

    fun domainToEntity(type: Type): TypeEntity

    fun entityToDomain(typeEntity: TypeEntity): Type
}
