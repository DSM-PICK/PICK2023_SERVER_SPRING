package com.pickdsm.pickserverspring.domain.afterschool.persistence.adapter

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.mapper.AfterSchoolMapper
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.QAfterSchoolEntity.afterSchoolEntity
import com.pickdsm.pickserverspring.domain.afterschool.persistence.vo.QQueryAfterSchoolRoomVO
import com.pickdsm.pickserverspring.domain.afterschool.persistence.vo.QueryAfterSchoolRoomVO
import com.pickdsm.pickserverspring.domain.afterschool.spi.AfterSchoolSpi
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

    override fun queryAfterSchoolClassroomListByFloor(floor: Int): List<QueryAfterSchoolRoomVO> =
        jpaQueryFactory
            .select(
                QQueryAfterSchoolRoomVO(
                    classroomEntity.id,
                    classroomEntity.name,
                    afterSchoolEntity.afterSchoolName,
                )
            )
            .from(afterSchoolEntity)
            .innerJoin(afterSchoolEntity.classroomEntity, classroomEntity)
            .on(afterSchoolEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(afterSchoolEntity.classroomEntity.floor.eq(floor))
            .fetch()

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
