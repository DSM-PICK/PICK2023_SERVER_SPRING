package com.pickdsm.pickserverspring.domain.classroom.persistence.entity

import com.pickdsm.pickserverspring.domain.application.persistence.entity.StatusEntity
import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Table(name = "tbl_classroom_movement")
@Entity
class ClassroomMovementEntity(

    override val id: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", columnDefinition = "BINARY(16)", nullable = false)
    val classroomEntity: ClassroomEntity,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", columnDefinition = "BINARY(16)", nullable = false)
    val statusEntity: StatusEntity,

) : BaseUUIDEntity(id)
