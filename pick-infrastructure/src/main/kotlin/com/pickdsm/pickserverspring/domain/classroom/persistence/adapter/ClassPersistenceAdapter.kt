package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomEntity.classroomEntity
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.UUID

@Adapter
class ClassPersistenceAdapter(
    private val classroomRepository: ClassroomRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : ClassroomSpi {

    override fun queryClassroomById(classroomId: UUID): Classroom =
        classroomRepository.findClassroomEntityById(classroomId)
            ?: throw ClassroomNotFoundException

    override fun queryAllClassroomListByFloor(floor: Int): List<ClassroomElement> =
        jpaQueryFactory
            .selectFrom(classroomEntity)
            .where(classroomEntity.floor.eq(floor))
            .fetch()
            .map {
                ClassroomElement(
                    id = it.id,
                    name = it.name,
                    description = ""
                )
            }

    override fun querySelfStudyClassroomListByFloor(floor: Int): List<ClassroomElement> =
        jpaQueryFactory
            .selectFrom(classroomEntity)
            .where(
                classroomEntity.floor.eq(floor),
                classroomEntity.grade.isNotNull,
            ) // 교실로 쓰이는 반은 학년이 존재
            .fetch()
            .map {
                ClassroomElement(
                    id = it.id,
                    name = it.name,
                    description = "", // 교실은 별다른 설명 없음
                )
            }
}
