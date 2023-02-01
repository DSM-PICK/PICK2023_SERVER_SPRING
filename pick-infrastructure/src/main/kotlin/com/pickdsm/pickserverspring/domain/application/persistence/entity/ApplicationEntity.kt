package com.pickdsm.pickserverspring.domain.application.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "tbl_application")
@Entity
class ApplicationEntity(

    override val id: UUID,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val studentId: UUID,

    @Column(columnDefinition = "DATE", nullable = false)
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
) : BaseUUIDEntity(id) {

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    @ColumnDefault("0")
    var isStatus = isStatus
        protected set

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    @ColumnDefault("0")
    var isPermission = isPermission
        protected set

    fun changePermission(): ApplicationEntity {
        this.isPermission = true
        return this
    }
}
