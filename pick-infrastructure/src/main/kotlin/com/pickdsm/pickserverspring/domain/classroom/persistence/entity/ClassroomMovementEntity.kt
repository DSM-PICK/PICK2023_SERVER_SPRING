package com.pickdsm.pickserverspring.domain.classroom.persistence.entity

import com.pickdsm.pickserverspring.domain.application.persistence.entity.StatusEntity
import java.io.Serializable
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

@Table(name = "tbl_classroom_movement")
@Entity
class ClassroomMovementEntity(

    @Id
    val id: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", columnDefinition = "BINARY(16)")
    val statusEntity: StatusEntity,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", columnDefinition = "BINARY(16)")
    val classroomEntity: ClassroomEntity,
) : Serializable {

    fun getClassroomId(): UUID = classroomEntity.id
}
