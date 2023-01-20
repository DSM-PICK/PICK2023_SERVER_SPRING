package com.pickdsm.pickserverspring.domain.club.persistence.entity

import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.ClassroomEntity
import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import org.hibernate.annotations.ColumnDefault
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Table(name = "tbl_club")
@Entity
class ClubEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    @ColumnDefault("''")
    val name: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", columnDefinition = "BINARY(16)")
    val classroomEntity: ClassroomEntity
) : BaseUUIDEntity(id) {

    fun getClassroomId(): UUID = classroomEntity.id
}
