package com.pickdsm.pickserverspring.domain.afterschool.persistence.entity

import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomEntity
import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Column
import javax.persistence.OneToOne
import javax.persistence.FetchType
import javax.persistence.JoinColumn

@Table(name = "tbl_after_school_info")
@Entity
class AfterSchoolInfoEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    @ColumnDefault("''")
    val afterSchoolName: String,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val teacherId: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", columnDefinition = "BINARY(16)", nullable = false)
    val classroomEntity: ClassroomEntity,

) : BaseUUIDEntity(id)
