package com.pickdsm.pickserverspring.domain.selfstudydirector.mapper

import com.pickdsm.pickserverspring.domain.selfstudydirector.Type
import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.TypeEntity
import org.springframework.stereotype.Component

@Component
class TypeMapperImpl : TypeMapper {

    override fun domainToEntity(type: Type): TypeEntity {
        return TypeEntity(
            id = type.id,
            date = type.date,
            type = type.type,
        )
    }

    override fun entityToDomain(typeEntity: TypeEntity): Type {
        return Type(
            id = typeEntity.id,
            date = typeEntity.date,
            type = typeEntity.type,
        )
    }
}
