package com.pickdsm.pickserverspring.domain.application.persistence.entity

import org.hibernate.annotations.ColumnDefault
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

@IdClass(ApplicationEntity::class)
@Table(name = "tbl_application")
@Entity
class ApplicationEntity(

    @MapsId(value = "tbl_status_id")
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tbl_status_id", columnDefinition = "BINARY(16)")
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

) : Serializable {

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
