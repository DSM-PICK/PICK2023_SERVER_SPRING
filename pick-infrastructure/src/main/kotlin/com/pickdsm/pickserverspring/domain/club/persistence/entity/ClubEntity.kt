package com.pickdsm.pickserverspring.domain.club.persistence.entity

import com.pickdsm.pickserverspring.global.entity.BaseUUIDEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Table(name = "tbl_club")
@Entity
class ClubEntity(

    override val id: UUID,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val studentId: UUID,

    clubInfoEntity: ClubInfoEntity
) : BaseUUIDEntity(id) {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_info_id", columnDefinition = "BINARY(16)", nullable = false)
    var clubInfoEntity = clubInfoEntity
        protected set
}
