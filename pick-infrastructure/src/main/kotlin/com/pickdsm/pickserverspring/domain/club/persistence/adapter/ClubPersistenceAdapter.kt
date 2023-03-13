package com.pickdsm.pickserverspring.domain.club.persistence.adapter

import com.pickdsm.pickserverspring.domain.classroom.persistence.entity.QClassroomEntity.classroomEntity
import com.pickdsm.pickserverspring.domain.club.Club
import com.pickdsm.pickserverspring.domain.club.ClubInfo
import com.pickdsm.pickserverspring.domain.club.mapper.ClubInfoMapper
import com.pickdsm.pickserverspring.domain.club.mapper.ClubMapper
import com.pickdsm.pickserverspring.domain.club.persistence.ClubInfoRepository
import com.pickdsm.pickserverspring.domain.club.persistence.ClubRepository
import com.pickdsm.pickserverspring.domain.club.persistence.entity.QClubEntity.clubEntity
import com.pickdsm.pickserverspring.domain.club.persistence.entity.QClubInfoEntity.clubInfoEntity
import com.pickdsm.pickserverspring.domain.club.persistence.vo.QQueryClubRoomVO
import com.pickdsm.pickserverspring.domain.club.persistence.vo.QueryClubRoomVO
import com.pickdsm.pickserverspring.domain.club.spi.ClubSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.*

@Adapter
class ClubPersistenceAdapter(
    private val clubMapper: ClubMapper,
    private val clubInfoMapper: ClubInfoMapper,
    private val clubRepository: ClubRepository,
    private val clubInfoRepository: ClubInfoRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : ClubSpi {

    override fun queryClubClassroomListByFloor(floor: Int): List<QueryClubRoomVO> =
        jpaQueryFactory
            .select(
                QQueryClubRoomVO(
                    classroomEntity.id,
                    clubInfoEntity.id,
                    classroomEntity.name,
                    clubInfoEntity.name,
                ),
            )
            .from(clubInfoEntity)
            .innerJoin(clubInfoEntity.classroomEntity, classroomEntity)
            .on(clubInfoEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(clubInfoEntity.classroomEntity.floor.eq(floor))
            .orderBy(clubInfoEntity.name.asc())
            .fetch()

    override fun queryClubByClubId(clubId: UUID): Club? =
        jpaQueryFactory
            .selectFrom(clubEntity)
            .where(clubInfoEntity.id.eq(clubId))
            .fetchOne()
            ?.let(clubMapper::entityToDomain)

    override fun queryClubInfoByClubId(clubId: UUID): ClubInfo? =
        jpaQueryFactory
            .selectFrom(clubInfoEntity)
            .where(clubInfoEntity.id.eq(clubId))
            .fetchOne()
            ?.let(clubInfoMapper::entityToDomain)

    override fun queryClubListByClassroomId(classroomId: UUID): List<Club> =
        jpaQueryFactory
            .selectFrom(clubEntity)
            .innerJoin(clubInfoEntity.classroomEntity, classroomEntity)
            .on(clubInfoEntity.classroomEntity.id.eq(classroomEntity.id))
            .where(clubInfoEntity.classroomEntity.id.eq(classroomId))
            .fetch()
            .map(clubMapper::entityToDomain)

    override fun queryStudentIdListByClubId(clubId: UUID): List<UUID> =
        jpaQueryFactory
            .select(clubEntity.studentId)
            .from(clubEntity)
            .join(clubInfoEntity)
            .on(clubInfoEntity.id.eq(clubId))
            .where(clubEntity.id.eq(clubId))
            .fetch()

    override fun queryClubStudentIdListByFloor(floor: Int?): List<UUID> =
        jpaQueryFactory
            .select(clubEntity.studentId)
            .from(clubEntity)
            .join(clubInfoEntity)
            .on(clubEntity.id.eq(clubInfoEntity.id))
            .join(classroomEntity)
            .on(clubInfoEntity.classroomEntity.eq(classroomEntity))
            .where(classroomEntity.floor.eq(floor))
            .fetch()

    override fun queryClubListByClubId(clubId: UUID): List<Club> =
        jpaQueryFactory
            .selectFrom(clubEntity)
            .join(clubInfoEntity)
            .on(clubEntity.clubInfoEntity.id.eq(clubInfoEntity.id))
            .where(clubInfoEntity.id.eq(clubId))
            .fetch()
            .map(clubMapper::entityToDomain)

    override fun queryClubInfoListByClubId(clubId: UUID): List<ClubInfo> =
        jpaQueryFactory
            .selectFrom(clubInfoEntity)
            .where(clubInfoEntity.id.eq(clubId))
            .fetch()
            .map(clubInfoMapper::entityToDomain)

    override fun queryClubByStudentId(studentId: UUID): Club? =
        jpaQueryFactory
            .selectFrom(clubEntity)
            .where(clubEntity.studentId.eq(studentId))
            .fetchOne()
            ?.let(clubMapper::entityToDomain)

    override fun saveClub(club: Club) {
        clubRepository.save(clubMapper.domainToEntity(club))
    }

    override fun saveClubInfo(clubInfo: ClubInfo) {
        clubInfoRepository.save(clubInfoMapper.domainToEntity(clubInfo))
    }

    override fun deleteClub(club: Club) {
        clubRepository.delete(clubMapper.domainToEntity(club))
    }
}
