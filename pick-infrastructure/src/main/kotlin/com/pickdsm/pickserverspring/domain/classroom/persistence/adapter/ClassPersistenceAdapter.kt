package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomEntity.classroomEntity
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.UUID

@Adapter
class ClassPersistenceAdapter(
    private val classroomRepository: ClassroomRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : ClassroomSpi {

    override fun queryClassroomById(classroomId: UUID): Classroom =
        classroomRepository.findClassroomEntityById(classroomId) ?: throw ClassroomNotFoundException

    override fun queryClassroomListByFloor(floor: Int): List<ClassroomElement> {
        return jpaQueryFactory
            .selectFrom(classroomEntity)
            .where(floorEq(floor))
            .fetch()
            .map { classroom ->
                ClassroomElement(
                    id = classroom.id,
                    name = classroom.name,
                )
            }
    }

    private fun floorEq(floor: Int): BooleanExpression = classroomEntity.floor.eq(floor)
}
