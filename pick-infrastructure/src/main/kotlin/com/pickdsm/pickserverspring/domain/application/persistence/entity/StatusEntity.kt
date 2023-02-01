package com.pickdsm.pickserverspring.domain.application.persistence.entity

import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table
import javax.persistence.Enumerated
import javax.persistence.EnumType

@Table(name = "tbl_status")
@Entity
class StatusEntity(

    override val id: UUID,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val studentId: UUID,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val teacherId: UUID,

    @Column(columnDefinition = "DATE", nullable = false)
    val date: LocalDate,

    @Column(columnDefinition = "TIME", nullable = false)
    val startTime: LocalTime,

    @Column(columnDefinition = "TIME", nullable = false)
    val endTime: LocalTime,

    type: StatusType,
) : BaseUUIDEntity(id) {

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(12)", nullable = false)
    @ColumnDefault("''")
    var type = type
        protected set
}
