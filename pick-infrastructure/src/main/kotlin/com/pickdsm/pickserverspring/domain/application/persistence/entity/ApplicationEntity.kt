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
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

@Table(name = "tbl_application")
@Entity
class ApplicationEntity(

    @Id
    val id: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", columnDefinition = "BINARY(16)")
    val statusEntity: StatusEntity,

    val studentId: UUID,

    @Column(nullable = false)
    val date: LocalDate = LocalDate.now(),

    @Column(columnDefinition = "TIME", nullable = false)
    val startTime: LocalTime,

    @Column(columnDefinition = "TIME", nullable = false)
    val endTime: LocalTime,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    @ColumnDefault("''")
    val reason: String,

    isStatus: Boolean,

    isPermission: Boolean,
) : Serializable {

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    @ColumnDefault("0")
    var isStatus = isStatus
        protected set

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    @ColumnDefault("0")
    var isPermission = isPermission
        protected set
}
