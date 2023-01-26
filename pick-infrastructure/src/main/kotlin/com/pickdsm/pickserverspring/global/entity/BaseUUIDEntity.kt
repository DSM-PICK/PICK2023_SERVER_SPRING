package com.pickdsm.pickserverspring.global.entity

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseUUIDEntity(

    @Id
    @GeneratedValue(generator = "UUIDv7")
    @GenericGenerator(name = "UUIDv7", strategy = "com.pickdsm.pickserverspring.common.uuid.UUIDv7Generator")
    @Type(type = "uuid-char")
    val id: UUID?,
)
