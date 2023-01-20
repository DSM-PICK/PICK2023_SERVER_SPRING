package com.pickdsm.pickserverspring.domain.classroom.persistence.entity

import com.pickdsm.pickserverspring.domain.application.persistence.entity.StatusEntity
import java.io.Serializable
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

@IdClass(ClassroomMovementEntity::class)
@Table(name = "tbl_classroom_movement")
@Entity
class ClassroomMovementEntity(

    @MapsId(value = "tbl_status_id")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tbl_status_id", columnDefinition = "BINARY(16)")
    @Id
    val statusEntity: StatusEntity,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tbl_classroom_id", columnDefinition = "BINARY(16)")
    val classroomEntity: ClassroomEntity
) : Serializable {
    fun getStatusId(): UUID = statusEntity.id

    fun getClassroomId(): UUID = classroomEntity.id
}
