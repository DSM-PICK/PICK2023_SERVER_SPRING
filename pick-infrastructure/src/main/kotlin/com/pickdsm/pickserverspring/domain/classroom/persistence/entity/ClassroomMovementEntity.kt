package com.pickdsm.pickserverspring.domain.classroom.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Table(name = "tbl_classroom_movement")
@Entity
class ClassroomMovementEntity(

    override val id: UUID,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val studentId: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", columnDefinition = "BINARY(16)", nullable = false)
    val classroomEntity: ClassroomEntity,
) : BaseUUIDEntity(id) {

    fun getClassroomId(): UUID = classroomEntity.id
}
