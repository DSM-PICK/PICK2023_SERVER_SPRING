package com.pickdsm.pickserverspring.global.entity

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseUUIDEntity(

    @Id
    @GeneratedValue(generator = "UUIDv7")
    @GenericGenerator(name = "UUIDv7", strategy = "com.pickdsm.pickserverspring.common.uuid.CustomUUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID,
)
