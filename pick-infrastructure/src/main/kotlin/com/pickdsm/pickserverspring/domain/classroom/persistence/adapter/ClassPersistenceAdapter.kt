package com.pickdsm.pickserverspring.domain.classroom.persistence.adapter

import com.pickdsm.pickserverspring.domain.classroom.Classroom
import com.pickdsm.pickserverspring.domain.classroom.exception.ClassroomNotFoundException
import com.pickdsm.pickserverspring.domain.classroom.mapper.ClassroomMapper
import com.pickdsm.pickserverspring.domain.classroom.persistence.ClassroomRepository
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomEntity.classroomEntity
import com.pickdsm.pickserverspring.domain.classroom.persistence.vo.QQueryClassroomVO
import com.pickdsm.pickserverspring.domain.classroom.persistence.vo.QueryClassroomVO
import com.pickdsm.pickserverspring.domain.classroom.spi.ClassroomSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.*

@Adapter
class ClassPersistenceAdapter(
    private val classroomMapper: ClassroomMapper,
    private val classroomRepository: ClassroomRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : ClassroomSpi {

    override fun queryClassroomById(classroomId: UUID): Classroom? =
        classroomRepository.findClassroomEntityById(classroomId)
            ?: throw ClassroomNotFoundException

    override fun queryClassroomListByFloorAndByType(floor: Int, classroomType: String): List<QueryClassroomVO> =
        jpaQueryFactory
            .select(
                QQueryClassroomVO(
                    classroomEntity.id,
                    classroomEntity.name,
                ),
            )
            .from(classroomEntity)
            .where(classroomEntity.floor.eq(floor))
            .orderBy(classroomEntity.name.asc())
            .fetch()

    override fun queryClassroomGradeByFloor(floor: Int): Int? =
        jpaQueryFactory
            .select(classroomEntity.grade)
            .from(classroomEntity)
            .where(classroomEntity.floor.eq(floor))
            .fetchFirst()

    override fun queryClassroomByGradeAndClassNum(grade: Int?, classNum: Int?): Classroom? =
        jpaQueryFactory
            .selectFrom(classroomEntity)
            .where(
                classroomEntity.grade.eq(grade),
                classroomEntity.classNum.eq(classNum),
            )
            .fetchFirst()
            ?.let(classroomMapper::entityToDomain)
}
