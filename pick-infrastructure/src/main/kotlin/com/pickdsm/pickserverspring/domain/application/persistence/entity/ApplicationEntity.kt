package com.pickdsm.pickserverspring.domain.application.persistence.entity

import org.hibernate.annotations.ColumnDefault
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Table(name = "tbl_application")
@Entity
class ApplicationEntity(

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", columnDefinition = "BINARY(16)")
    @Id
    val statusEntity: StatusEntity,

    date: LocalDate,

    @Column(columnDefinition = "TIME", nullable = false)
    val startTime: LocalTime,

    @Column(columnDefinition = "TIME", nullable = false)
    val endTime: LocalTime,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    @ColumnDefault("''")
    val reason: String,

    isStatus: Boolean,

    isPermission: Boolean

) {

    @Column(nullable = false)
    val date: LocalDate = LocalDate.now()

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    @ColumnDefault("0")
    var isStatus = isStatus
        protected set

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    @ColumnDefault("0")
    var isPermission = isPermission
        protected set

    fun getStatusId(): UUID = statusEntity.id
}
