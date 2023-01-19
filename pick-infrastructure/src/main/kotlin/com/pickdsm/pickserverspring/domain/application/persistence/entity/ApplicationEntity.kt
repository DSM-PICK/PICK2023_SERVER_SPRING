package com.pickdsm.pickserverspring.domain.application.persistence.entity

import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Table(name = "tbl_application")
@Entity
class ApplicationEntity(

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", columnDefinition = "BINARY(16)")
    @Id
    val statusEntity: StatusEntity,

    date: LocalDate,

    @field:Column(columnDefinition = "TIME", nullable = false)
    @field:NotNull
    val startTime: LocalTime,

    @field:Column(columnDefinition = "TIME", nullable = false)
    @field:NotNull
    val endTime: LocalTime,

    @field:Column(columnDefinition = "VARCHAR(255)", nullable = false)
    @field:NotNull
    val reason: String = "",

    isStatus: Boolean,

    isPermission: Boolean

) {

    @field:Column(nullable = false)
    val date: LocalDate = LocalDate.now()

    @field:Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
    @field:NotNull
    var isStatus = isStatus
        protected set

    @field:Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
    @field:NotNull
    var isPermission = isPermission
        protected set

    fun getStatusId(): UUID = statusEntity.id
}
