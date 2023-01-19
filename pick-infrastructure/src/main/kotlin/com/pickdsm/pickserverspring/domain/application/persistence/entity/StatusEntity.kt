package com.pickdsm.pickserverspring.domain.application.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(name = "tbl_status")
@Entity
class StatusEntity (

    override val id: UUID,

    @field:Column(columnDefinition = "VARCHAR(12)")
    @field:NotNull
    val type: String = "",

    date: LocalDate
) : BaseUUIDEntity(id) {

    @CreatedDate
    @field:NotNull
    var date: LocalDate = LocalDate.now()
}