package com.pickdsm.pickserverspring.domain.club.persistence.adapter

import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomEntity.classroomEntity
import com.pickdsm.pickserverspring.domain.club.Club
import com.pickdsm.pickserverspring.domain.club.mapper.ClubMapper
import com.pickdsm.pickserverspring.domain.club.persistence.ClubRepository
import com.pickdsm.pickserverspring.domain.club.persistence.entity.QClubEntity.clubEntity
import com.pickdsm.pickserverspring.domain.club.persistence.vo.QQueryClubRoomVO
import com.pickdsm.pickserverspring.domain.club.persistence.vo.QueryClubRoomVO
import com.pickdsm.pickserverspring.domain.club.spi.ClubSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.UUID

@Adapter
class ClubPersistenceAdapter(
    private val clubMapper: ClubMapper,
    private val clubRepository: ClubRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : ClubSpi {

    override fun queryClubClassroomListByFloor(floor: Int): List<QueryClubRoomVO> =
        jpaQueryFactory
            .select(
                QQueryClubRoomVO(
                    classroomEntity.id,
                    clubEntity.id,
                    classroomEntity.name,
                    clubEntity.name,
                ),
            )
            .from(clubEntity)
            .innerJoin(clubEntity.classroomEntity, classroomEntity)
            .on(clubEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(clubEntity.classroomEntity.floor.eq(floor))
            .orderBy(clubEntity.name.asc())
            .fetch()

    override fun queryClubByClubId(clubId: UUID): Club? =
        jpaQueryFactory
            .selectFrom(clubEntity)
            .where(clubEntity.id.eq(clubId))
            .fetchOne()
            ?.let(clubMapper::entityToDomain)

    override fun queryClubListByClassroomId(classroomId: UUID): List<Club> =
        jpaQueryFactory
            .selectFrom(clubEntity)
            .innerJoin(clubEntity.classroomEntity, classroomEntity)
            .on(clubEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(clubEntity.classroomEntity.id.eq(classroomId))
            .fetch()
            .map(clubMapper::entityToDomain)

    override fun queryStudentIdListByClubId(clubId: UUID): List<UUID> =
        jpaQueryFactory
            .select(clubEntity.studentId)
            .from(clubEntity)
            .where(clubEntity.id.eq(clubId))
            .fetch()

    override fun queryClubStudentIdListByFloor(floor: Int?): List<UUID> =
        jpaQueryFactory
            .select(clubEntity.studentId)
            .from(clubEntity)
            .innerJoin(clubEntity.classroomEntity, classroomEntity)
            .on(clubEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(clubEntity.classroomEntity.floor.eq(floor))
            .fetch()

    override fun queryClubListByClubId(clubId: UUID): List<Club> =
        jpaQueryFactory
            .selectFrom(clubEntity)
            .where(clubEntity.id.eq(clubId))
            .fetch()
            .map(clubMapper::entityToDomain)

    override fun queryClubByStudentId(studentId: UUID): Club? =
        jpaQueryFactory
            .selectFrom(clubEntity)
            .where(clubEntity.studentId.eq(studentId))
            .fetchOne()
            ?.let(clubMapper::entityToDomain)

    override fun saveClub(club: Club) {
        clubRepository.save(clubMapper.domainToEntity(club))
    }

    override fun deleteClub(club: Club) {
        clubRepository.delete(clubMapper.domainToEntity(club))
    }
}
