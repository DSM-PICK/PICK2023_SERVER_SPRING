package com.pickdsm.pickserverspring.domain.club.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "tbl_club")
@Entity
class ClubEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    @ColumnDefault("''")
    val name: String,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val headId: UUID,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val teacherId: UUID,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val classroomId: UUID,
) : BaseUUIDEntity(id)
