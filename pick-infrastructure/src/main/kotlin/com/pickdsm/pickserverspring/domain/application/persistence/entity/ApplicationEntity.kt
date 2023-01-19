package com.pickdsm.pickserverspring.domain.application.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@EntityListeners(value = [AuditingEntityListener::class])
@Table(name = "tbl_application")
@Entity
class ApplicationEntity (

    override val id: UUID,

    date: LocalDate,

    @field:Column(columnDefinition = "TIME")
    @field:NotNull
    val startTime: LocalTime,

    @field:Column(columnDefinition = "TIME")
    @field:NotNull
    val endTime: LocalTime,

    @field:Column(columnDefinition = "VARCHAR(255)")
    @field:NotNull
    val reason: String = "",

    isStatus: Boolean,

    isPermission: Boolean

) : BaseUUIDEntity(id) {

    @CreatedDate
    @field:NotNull
    var date: LocalDate = date
        protected set

    @field:Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    @field:NotNull
    var isStatus: Boolean = isStatus
        protected set

    @field:Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    @field:NotNull
    var isPermission: Boolean = isPermission
        protected set
}
