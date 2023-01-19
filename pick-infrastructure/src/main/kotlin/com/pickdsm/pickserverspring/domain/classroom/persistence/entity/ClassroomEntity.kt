package com.pickdsm.pickserverspring.domain.classroom.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@EntityListeners(value = [AuditingEntityListener::class])
@Table(name = "tbl_classroom")
@Entity
class ClassroomEntity (

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(255) DEFAULT ''", nullable = false)
    val name: String,

    @Column(columnDefinition = "INT")
    val floor: Int
) : BaseUUIDEntity(id)
