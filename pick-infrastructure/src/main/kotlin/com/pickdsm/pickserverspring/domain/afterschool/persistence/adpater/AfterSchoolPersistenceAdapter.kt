package com.pickdsm.pickserverspring.domain.afterschool.persistence.adpater

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.mapper.AfterSchoolMapper
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.QAfterSchoolEntity.afterSchoolEntity
import com.pickdsm.pickserverspring.domain.afterschool.spi.AfterSchoolSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.*
import javax.persistence.LockModeType

@Adapter
class AfterSchoolPersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val afterSchoolMapper: AfterSchoolMapper,
) : AfterSchoolSpi {

    override fun deleteByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID) {
        jpaQueryFactory
            .delete(afterSchoolEntity)
            .where(
                afterSchoolEntity.id.eq(afterSchoolId),
                afterSchoolEntity.studentId.eq(studentId)
            )
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .execute()
    }

    override fun findByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID): AfterSchool? {
        return jpaQueryFactory
            .selectFrom(afterSchoolEntity)
            .where(
                afterSchoolEntity.id.eq(afterSchoolId),
                afterSchoolEntity.studentId.eq(studentId)
            )
            .setLockMode(LockModeType.PESSIMISTIC_READ)
            .fetchOne()
            ?.let(afterSchoolMapper::entityToDomain)
    }
}
