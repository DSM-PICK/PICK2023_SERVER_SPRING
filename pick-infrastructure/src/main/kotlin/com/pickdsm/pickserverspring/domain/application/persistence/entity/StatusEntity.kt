package com.pickdsm.pickserverspring.domain.application.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "tbl_status")
@Entity
class StatusEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(12)", nullable = false)
    @ColumnDefault("''")
    val type: String,

    @Column(nullable = false)
    val date: LocalDate = LocalDate.now(),

    val studentId: UUID,
) : BaseUUIDEntity(id)
