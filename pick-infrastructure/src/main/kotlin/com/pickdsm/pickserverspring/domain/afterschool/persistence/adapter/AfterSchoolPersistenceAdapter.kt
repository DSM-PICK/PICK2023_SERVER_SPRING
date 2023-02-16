package com.pickdsm.pickserverspring.domain.afterschool.persistence.adapter

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.mapper.AfterSchoolMapper
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.QAfterSchoolEntity.afterSchoolEntity
import com.pickdsm.pickserverspring.domain.afterschool.spi.AfterSchoolSpi
import com.pickdsm.pickserverspring.domain.classroom.api.dto.response.ClassroomElement
import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomEntity.classroomEntity
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.UUID
import javax.persistence.LockModeType

@Adapter
class AfterSchoolPersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val afterSchoolMapper: AfterSchoolMapper,
) : AfterSchoolSpi {

    override fun queryAfterSchoolClassroomListByFloor(floor: Int): List<ClassroomElement> =
        jpaQueryFactory
            .selectFrom(afterSchoolEntity)
            .innerJoin(afterSchoolEntity.classroomEntity, classroomEntity)
            .on(afterSchoolEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(afterSchoolEntity.classroomEntity.floor.eq(floor))
            .fetch()
            .map {
                ClassroomElement(
                    id = it.classroomEntity.id,
                    name = it.classroomEntity.name,
                    description = it.afterSchoolName,
                )
            }

    override fun deleteByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID) {
        jpaQueryFactory
            .delete(afterSchoolEntity)
            .where(
                afterSchoolEntity.id.eq(afterSchoolId),
                afterSchoolEntity.studentId.eq(studentId),
            )
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .execute()
    }

    override fun findByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID): AfterSchool? =
        jpaQueryFactory
            .selectFrom(afterSchoolEntity)
            .where(
                afterSchoolEntity.id.eq(afterSchoolId),
                afterSchoolEntity.studentId.eq(studentId),
            )
            .setLockMode(LockModeType.PESSIMISTIC_READ)
            .fetchOne()
            ?.let(afterSchoolMapper::entityToDomain)
}
