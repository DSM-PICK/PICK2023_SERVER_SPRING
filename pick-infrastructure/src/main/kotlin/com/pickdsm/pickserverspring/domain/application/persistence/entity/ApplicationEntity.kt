package com.pickdsm.pickserverspring.domain.application.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Column

@Table(name = "tbl_application")
@Entity
class ApplicationEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    @ColumnDefault("''")
    val reason: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", columnDefinition = "BINARY(16)", nullable = false)
    val statusEntity: StatusEntity,

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    @ColumnDefault("false")
    val isReturn: Boolean,

) : BaseUUIDEntity(id)
