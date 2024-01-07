package com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity

import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.common.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.Column
import javax.persistence.Enumerated
import javax.persistence.EnumType

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
