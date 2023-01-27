package com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity

import com.pickdsm.pickserverspring.domain.selfstudydirector.DirectorType
import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Table(name = "tbl_selfstudy_director")
@Entity
class SelfStudyDirectorEntity(

    override val id: UUID,

    @Column(nullable = false)
    val floor: Int,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val teacherId: UUID,

    @Column(columnDefinition = "DATE", nullable = false)
    val date: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(12)", nullable = false)
    @ColumnDefault("''")
    val type: DirectorType,
) : BaseUUIDEntity(id)
