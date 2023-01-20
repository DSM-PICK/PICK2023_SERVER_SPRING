package com.pickdsm.pickserverspring.global.entity

import com.github.f4b6a3.uuid.UuidCreator
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseUUIDEntity(

    @Id
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val id: UUID = UuidCreator.getTimeOrderedEpoch()
)
