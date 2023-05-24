package com.pickdsm.pickserverspring.domain.afterschool.persistence.adapter

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.AfterSchoolInfo
import com.pickdsm.pickserverspring.domain.afterschool.mapper.AfterSchoolInfoMapper
import com.pickdsm.pickserverspring.domain.afterschool.mapper.AfterSchoolMapper
import com.pickdsm.pickserverspring.domain.afterschool.persistence.AfterSchoolRepository
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.QAfterSchoolEntity.afterSchoolEntity
import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.QAfterSchoolInfoEntity.afterSchoolInfoEntity
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
    private val afterSchoolInfoMapper: AfterSchoolInfoMapper,
    private val afterSchoolRepository: AfterSchoolRepository,
) : AfterSchoolSpi {

    override fun queryAfterSchoolClassroomListByFloor(floor: Int): List<QueryAfterSchoolRoomVO> =
        jpaQueryFactory
            .select(
                QQueryAfterSchoolRoomVO(
                    classroomEntity.id,
                    afterSchoolInfoEntity.id,
                    classroomEntity.name,
                    afterSchoolInfoEntity.afterSchoolName,
                ),
            )
            .from(afterSchoolInfoEntity)
            .innerJoin(afterSchoolInfoEntity.classroomEntity, classroomEntity)
            .on(afterSchoolInfoEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(afterSchoolInfoEntity.classroomEntity.floor.eq(floor))
            .orderBy(afterSchoolInfoEntity.classroomEntity.name.asc())
            .fetch()

    override fun queryAfterSchoolClassroomByAfterSchoolName(afterSchoolName: String): QueryAfterSchoolRoomVO? =
        jpaQueryFactory
            .select(
                QQueryAfterSchoolRoomVO(
                    classroomEntity.id,
                    afterSchoolInfoEntity.id,
                    classroomEntity.name,
                    afterSchoolInfoEntity.afterSchoolName,
                ),
            )
            .from(afterSchoolInfoEntity)
            .innerJoin(afterSchoolInfoEntity.classroomEntity, classroomEntity)
            .on(afterSchoolInfoEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(afterSchoolInfoEntity.afterSchoolName.eq(afterSchoolName))
            .fetchOne()

    override fun queryAfterSchoolListByClassroomId(classroomId: UUID): List<AfterSchool> =
        jpaQueryFactory
            .selectFrom(afterSchoolEntity)
            .join(afterSchoolInfoEntity)
            .on(afterSchoolEntity.afterSchoolInfoEntity.eq(afterSchoolInfoEntity))
            .join(afterSchoolInfoEntity.classroomEntity, classroomEntity)
            .on(afterSchoolInfoEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(afterSchoolInfoEntity.classroomEntity.id.eq(classroomId))
            .fetch()
            .map(afterSchoolMapper::entityToDomain)

    override fun deleteByAfterSchoolIdAndStudentId(afterSchoolId: UUID) {
        jpaQueryFactory
            .delete(afterSchoolEntity)
            .where(afterSchoolEntity.id.eq(afterSchoolId))
            .execute()
    }

    override fun findByAfterSchoolIdAndStudentId(afterSchoolId: UUID, studentId: UUID): AfterSchool? =
        jpaQueryFactory
            .selectFrom(afterSchoolEntity)
            .innerJoin(
                afterSchoolEntity.afterSchoolInfoEntity,
                afterSchoolInfoEntity,
            )
            .where(
                afterSchoolEntity.afterSchoolInfoEntity.id.eq(afterSchoolId),
                afterSchoolEntity.studentId.eq(studentId),
            )
            .setLockMode(LockModeType.PESSIMISTIC_READ)
            .fetchOne()
            ?.let(afterSchoolMapper::entityToDomain)

    override fun findByAfterSchoolId(afterSchoolId: UUID): AfterSchool? {
        return jpaQueryFactory
            .selectFrom(afterSchoolEntity)
            .where(afterSchoolEntity.id.eq(afterSchoolId))
            .fetchOne()
            ?.let(afterSchoolMapper::entityToDomain)
    }

    override fun queryAfterSchoolInfoByAfterSchoolId(afterSchoolId: UUID): AfterSchoolInfo? {
        return jpaQueryFactory
            .selectFrom(afterSchoolInfoEntity)
            .where(afterSchoolInfoEntity.id.eq(afterSchoolId))
            .fetchOne()
            ?.let { afterSchoolInfoMapper.entityToDomain(it) }
    }

    override fun queryAfterSchoolStudentIdByFloor(floor: Int?): List<UUID> =
        jpaQueryFactory
            .select(afterSchoolEntity.studentId)
            .from(afterSchoolEntity)
            .join(afterSchoolInfoEntity)
            .on(afterSchoolEntity.afterSchoolInfoEntity.id.eq(afterSchoolInfoEntity.id))
            .join(afterSchoolInfoEntity.classroomEntity, classroomEntity)
            .on(afterSchoolInfoEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(afterSchoolInfoEntity.classroomEntity.floor.eq(floor))
            .fetch()

    override fun queryAfterSchoolListByAfterSchoolId(afterSchoolId: UUID): List<AfterSchool> =
        jpaQueryFactory
            .selectFrom(afterSchoolEntity)
            .join(afterSchoolInfoEntity)
            .on(afterSchoolEntity.afterSchoolInfoEntity.id.eq(afterSchoolInfoEntity.id))
            .where(afterSchoolInfoEntity.id.eq(afterSchoolId))
            .fetch()
            .map(afterSchoolMapper::entityToDomain)

    override fun queryAfterSchoolIdByStudentId(studentId: UUID): UUID? =
        jpaQueryFactory
            .select(afterSchoolEntity.id)
            .from(afterSchoolEntity)
            .where(afterSchoolEntity.studentId.eq(studentId))
            .fetchOne()

    override fun saveAll(afterSchools: List<AfterSchool>) {
        afterSchoolRepository.saveAll(
            afterSchools.map(afterSchoolMapper::domainToEntity),
        )
    }

    override fun existsAfterSchoolByStudentIds(afterSchoolStudentIds: List<UUID>): Boolean =
        jpaQueryFactory
            .selectOne()
            .from(afterSchoolEntity)
            .where(afterSchoolEntity.studentId.`in`(afterSchoolStudentIds))
            .fetchFirst() != null

    override fun queryAfterSchoolClassroomIdByStudentId(studentId: UUID): UUID? =
        jpaQueryFactory
            .select(afterSchoolEntity.afterSchoolInfoEntity.classroomEntity.id)
            .from(afterSchoolEntity)
            .where(afterSchoolEntity.studentId.eq(studentId))
            .join(afterSchoolInfoEntity)
            .on(afterSchoolEntity.afterSchoolInfoEntity.id.eq(afterSchoolInfoEntity.id))
            .join(classroomEntity)
            .on(afterSchoolInfoEntity.classroomEntity.id.eq(classroomEntity.id))
            .fetchOne()
}
