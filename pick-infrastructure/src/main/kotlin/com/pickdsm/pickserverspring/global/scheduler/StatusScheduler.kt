package com.pickdsm.pickserverspring.global.scheduler

import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.persistence.StatusRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class StatusScheduler(
    private val statusRepository: StatusRepository,
) {

    @Transactional
    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Seoul")
    fun changeStudentStatusDate() {
        val statusList = statusRepository.findAllByDate(LocalDate.now())
        val changeStatus = statusList
            .filter { statusEntity ->
                statusEntity.type == StatusType.EMPLOYMENT || statusEntity.type == StatusType.FIELD_TRIP
            }
            .map { statusEntity -> statusEntity.changeStatusDate() }
        statusRepository.saveAll(changeStatus)
    }
}
