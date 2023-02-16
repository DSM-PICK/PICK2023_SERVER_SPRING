package com.pickdsm.pickserverspring.domain.club.persistence.adapter

import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomEntity.classroomEntity
import com.pickdsm.pickserverspring.domain.club.persistence.entity.QClubEntity.clubEntity
import com.pickdsm.pickserverspring.domain.club.spi.ClubSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory

@Adapter
class ClubPersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
) : ClubSpi {

    override fun queryClubClassroomListByFloor(floor: Int): List<ClassroomElement> =
        jpaQueryFactory
            .selectFrom(clubEntity)
            .innerJoin(clubEntity.classroomEntity, classroomEntity)
            .on(clubEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(clubEntity.classroomEntity.floor.eq(floor))
            .fetch()
            .map {
                ClassroomElement(
                    id = it.classroomEntity.id,
                    name = it.classroomEntity.name,
                    description = it.name,
                )
            }
}
