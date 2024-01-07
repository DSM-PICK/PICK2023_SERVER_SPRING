package com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity

import com.pickdsm.pickserverspring.common.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Table(name = "tbl_selfstudy_director")
@Entity
class SelfStudyDirectorEntity(

    override val id: UUID,

    @Column(nullable = false)
    val floor: Int,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val teacherId: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", columnDefinition = "BINARY(16)", nullable = false)
    val typeEntity: TypeEntity,

    restrictionMovement: Boolean,

) : BaseUUIDEntity(id) {

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    @ColumnDefault("false")
    var restrictionMovement = restrictionMovement
        protected set

    fun setRestrictionMovementTrue() {
        this.restrictionMovement = true
    }
}
