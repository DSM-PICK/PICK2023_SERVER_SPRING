package com.pickdsm.pickserverspring.domain.application.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@EntityListeners(value = [AuditingEntityListener::class])
@Table(name = "tbl_application")
@Entity
class ApplicationEntity (


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @Id
    val id: StatusEntity,

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

) {

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
