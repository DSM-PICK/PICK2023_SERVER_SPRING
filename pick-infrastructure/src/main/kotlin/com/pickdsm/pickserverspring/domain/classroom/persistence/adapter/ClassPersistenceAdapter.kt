package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.mapper.ClassroomMapper
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomEntity.classroomEntity
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.UUID

@Adapter
class ClassPersistenceAdapter(
    private val classroomMapper: ClassroomMapper,
    private val classroomRepository: ClassroomRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : ClassroomSpi {

    override fun queryClassroomById(classroomId: UUID): Classroom =
        classroomRepository.findClassroomEntityById(classroomId)
            ?: throw ClassroomNotFoundException

    override fun queryClassroomListByFloor(floor: Int): List<Classroom> =
        jpaQueryFactory
            .selectFrom(classroomEntity)
            .where(classroomEntity.floor.eq(floor))
            .fetch()
            .map(classroomMapper::entityToDomain)
}

