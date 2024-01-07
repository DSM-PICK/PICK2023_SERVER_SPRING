package com.pickdsm.pickserverspring.domain.afterschool.persistence.entity

import com.pickdsm.pickserverspring.common.entity.BaseUUIDEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Table(name = "tbl_after_school")
@Entity
class AfterSchoolEntity(

    override val id: UUID,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val studentId: UUID,

    afterSchoolInfoEntity: AfterSchoolInfoEntity,
) : BaseUUIDEntity(id) {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_school_Info_id", columnDefinition = "BINARY(16)", nullable = false)
    var afterSchoolInfoEntity = afterSchoolInfoEntity
        protected set
}
