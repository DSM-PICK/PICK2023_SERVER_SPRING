package com.pickdsm.pickserverspring.domain.classroom.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
@Table(name = "tbl_classroom")
@Entity
class ClassroomEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    @ColumnDefault("''")
    val name: String,

    @Column(columnDefinition = "INT")
    val floor: Int,
) : BaseUUIDEntity(id)
