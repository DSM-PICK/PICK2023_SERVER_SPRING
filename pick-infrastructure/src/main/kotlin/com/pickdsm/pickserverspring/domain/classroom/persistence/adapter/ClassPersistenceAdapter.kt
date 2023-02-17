package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.ClassroomType
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomEntity.classroomEntity
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.*

@Adapter
class ClassPersistenceAdapter(
    private val classroomRepository: ClassroomRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : ClassroomSpi {

    override fun queryClassroomById(classroomId: UUID): Classroom =
        classroomRepository.findClassroomEntityById(classroomId)
            ?: throw ClassroomNotFoundException

    override fun queryClassroomListByFloorAndByType(floor: Int, classroomType: String): List<ClassroomElement> =
        jpaQueryFactory
            .selectFrom(classroomEntity)
            .where(checkClassroomType(classroomType), classroomEntity.floor.eq(floor))
            .fetch()
            .map {
                ClassroomElement(
                    id = it.id,
                    name = it.name,
                    description = "", // 교실은 별다른 설명 없음
                )
            }

    private fun checkClassroomType(classroomType: String): BooleanExpression? {
        if (classroomType == ClassroomType.SELF_STUDY.name) {
            return classroomEntity.grade.isNotNull
        }
        return null
    }
}
