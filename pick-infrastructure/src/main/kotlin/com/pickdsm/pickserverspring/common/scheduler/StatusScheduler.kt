package com.pickdsm.pickserverspring.common.scheduler

import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.persistence.StatusRepository
import com.pickdsm.pickserverspring.domain.application.persistence.entity.StatusEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Component
class StatusScheduler(
    private val statusRepository: StatusRepository,
) {

    @Transactional
    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Seoul")
    fun changeStudentStatusDate() {
        val statusList = statusRepository.findAllByTypeOrTypeAndDate(
            firstType = StatusType.EMPLOYMENT,
            secondType = StatusType.FIELD_TRIP,
            date = LocalDate.now(),
        )
        statusList.map(StatusEntity::changeStatusDate)
    }

    @Transactional
    @Scheduled(cron = "0 30 20 * * *", zone = "Asia/Seoul")
    fun saveEmploymentAndFieldTrip() {
        val statusList = statusRepository.findAllByTypeOrTypeAndDate(
            firstType = StatusType.EMPLOYMENT,
            secondType = StatusType.FIELD_TRIP,
            date = LocalDate.now(),
        )
        val changeList = statusList.map { statusEntity ->
            when (statusEntity.type) {
                StatusType.EMPLOYMENT_START -> {
                    saveToStatus(statusEntity, StatusType.EMPLOYMENT)
                }

                StatusType.FIELD_TRIP_START -> {
                    saveToStatus(statusEntity, StatusType.FIELD_TRIP)
                }

                else -> throw StatusNotFoundException
            }
        }
        statusRepository.saveAll(changeList)
    }

    private fun saveToStatus(statusEntity: StatusEntity, statusType: StatusType) =
        StatusEntity(
            id = UUID.randomUUID(),
            studentId = statusEntity.studentId,
            teacherId = statusEntity.teacherId,
            date = LocalDate.now(),
            startPeriod = 1,
            endPeriod = 10,
            type = statusType,
        )
}
