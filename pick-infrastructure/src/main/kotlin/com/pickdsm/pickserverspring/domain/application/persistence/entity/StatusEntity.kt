package com.pickdsm.pickserverspring.domain.application.persistence.entity

import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.common.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.Column
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

    @Column(columnDefinition = "INT", nullable = false)
    val startPeriod: Int,

    @Column(columnDefinition = "INT", nullable = false)
    val endPeriod: Int,

    date: LocalDate,

    type: StatusType,

) : BaseUUIDEntity(id) {

    @Column(columnDefinition = "DATE", nullable = false)
    var date = date
        protected set

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(16)", nullable = false)
    @ColumnDefault("'ATTENDANCE'")
    var type = type
        protected set

    fun changeStatusDate() {
        this.date = LocalDate.now().plusDays(1L)
    }
}
