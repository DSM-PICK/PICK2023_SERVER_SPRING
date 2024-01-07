package com.pickdsm.pickserverspring.domain.classroom.persistence.entity

import com.pickdsm.pickserverspring.common.entity.BaseUUIDEntity
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

    @Column(columnDefinition = "INT", nullable = false)
    val floor: Int,

    @Column(columnDefinition = "INT")
    val grade: Int?,

    @Column(columnDefinition = "INT")
    val classNum: Int?,

    homeroomTeacherId: UUID?,
) : BaseUUIDEntity(id) {

    @Column(columnDefinition = "BINARY(16)")
    var homeroomTeacherId = homeroomTeacherId
        protected set
}
