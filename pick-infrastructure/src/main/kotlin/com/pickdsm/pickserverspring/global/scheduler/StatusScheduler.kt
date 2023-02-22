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
        val statusList = statusRepository.findAllByTypeOrTypeAndDate(
            employment = StatusType.EMPLOYMENT,
            fieldTrip = StatusType.FIELD_TRIP,
            date = LocalDate.now(),
        )
        statusList.map { statusEntity -> statusEntity.changeStatusDate() }
    }
}
