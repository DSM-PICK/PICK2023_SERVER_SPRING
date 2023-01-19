package com.pickdsm.pickserverspring.domain.classroom.persistence.entity

import com.pickdsm.pickserverspring.domain.application.persistence.entity.StatusEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@EntityListeners(value = [AuditingEntityListener::class])
@Table(name = "tbl_classroom_movement")
@Entity
class ClassroomMovementEntity (

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", columnDefinition = "BINARY(16)")
    @Id
    val statusEntity: StatusEntity,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", columnDefinition = "BINARY(16)")
    val classroomEntity: ClassroomEntity
) {
    fun getStatusId(): UUID = statusEntity.id

    fun getClassroomId(): UUID = classroomEntity.id
}

