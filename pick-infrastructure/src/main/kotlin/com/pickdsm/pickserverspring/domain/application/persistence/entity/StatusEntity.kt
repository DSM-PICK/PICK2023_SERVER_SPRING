package com.pickdsm.pickserverspring.domain.application.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull


@Table(name = "tbl_status")
@Entity
class StatusEntity (

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(12) DEFAULT ''", nullable = false)
    val type: String,

    date: LocalDate
) : BaseUUIDEntity(id) {

    @Column(nullable = false)
    val date: LocalDate = LocalDate.now()
}