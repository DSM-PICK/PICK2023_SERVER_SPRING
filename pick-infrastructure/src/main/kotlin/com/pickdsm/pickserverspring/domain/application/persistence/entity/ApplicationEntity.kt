package com.pickdsm.pickserverspring.domain.application.persistence.entity

import org.hibernate.annotations.ColumnDefault
import java.time.LocalTime
import java.util.UUID
import javax.persistence.*

@Table(name = "tbl_application")
@Entity
class ApplicationEntity(

    @Id
    val statusId: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "status_id", columnDefinition = "BINARY(16)")
    val statusEntity: StatusEntity,

    @Column(columnDefinition = "TIME", nullable = false)
    val desiredStartTime: LocalTime,

    @Column(columnDefinition = "TIME", nullable = false)
    val desiredEndTime: LocalTime,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    @ColumnDefault("''")
    val reason: String,
)
