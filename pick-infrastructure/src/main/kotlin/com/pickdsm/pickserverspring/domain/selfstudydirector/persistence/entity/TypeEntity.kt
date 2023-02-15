package com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity

import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Table(name = "tbl_type")
@Entity
class TypeEntity(

    override val id: UUID,

    @Column(columnDefinition = "DATE", nullable = false)
    val date: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(13)", nullable = false)
    @ColumnDefault("''")
    val type: DirectorType,

) : BaseUUIDEntity(id)
