package com.pickdsm.pickserverspring.domain.admin.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.admin.api.AdminApi
import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest
import com.pickdsm.pickserverspring.domain.admin.api.dto.request.DomainUpdateStudentStatusOfClassRequest.DomainUpdateStudentElement
import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.exception.CannotChangeEmploymentException
import com.pickdsm.pickserverspring.domain.application.exception.StatusNotFoundException
import com.pickdsm.pickserverspring.domain.application.spi.QueryStatusSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.exception.TypeNotFoundException
import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.TimeQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.time.exception.TimeNotFoundException
import com.pickdsm.pickserverspring.domain.time.spi.QueryTimeSpi
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.time.LocalDate

@UseCase
class AdminUseCase(
    private val userSpi: UserSpi,
    private val statusCommandTeacherSpi: StatusCommandTeacherSpi,
    private val timeQueryTeacherSpi: TimeQueryTeacherSpi,
    private val queryTimeSpi: QueryTimeSpi,
    private val queryStatusSpi: QueryStatusSpi,
) : AdminApi {

    override fun updateStudentStatusOfClass(request: DomainUpdateStudentStatusOfClassRequest) {
        val teacherId = userSpi.getCurrentUserId()
        val statusList = queryStatusSpi.queryStatusListByToday()
        val timeList = timeQueryTeacherSpi.queryTime(LocalDate.now())
        val nowPeriod = queryTimeSpi.queryNowPeriod(timeList)
        val userIdList = request.userList.map { it.userId }
        val userInfoList = userSpi.queryUserInfo(userIdList)

        val changeStatusList: List<Status> = request.userList.map {
            val user = userInfoList.find { user -> user.id == it.userId }
                ?: throw UserNotFoundException
            val status = statusList.find { status -> status.studentId == it.userId }
                ?: throw StatusNotFoundException
            val time = timeList.timeList.find { time -> time.period == nowPeriod }
                ?: throw TimeNotFoundException

            when (it.status) {
                StatusType.ATTENDANCE -> {
                    status.changeStatusOfClass(
                        teacherId = teacherId,
                        startPeriod = time.period,
                        endPeriod = time.period,
                        type = it.status,
                    )
                }

                StatusType.EMPLOYMENT -> {
                    if (user.grade != 3) {
                        throw CannotChangeEmploymentException
                    }
                    status.changeStatusOfClass(
                        teacherId = teacherId,
                        startPeriod = 1,
                        endPeriod = 10,
                        type = it.status,
                    )
                }

                StatusType.FIELD_TRIP, StatusType.LEAVE -> {
                    status.changeStatusOfClass(
                        teacherId = teacherId,
                        startPeriod = time.period,
                        endPeriod = 10,
                        type = it.status,
                    )
                }

                else -> throw TypeNotFoundException
            }
        }
        statusCommandTeacherSpi.saveAllStatus(changeStatusList)
    }
}
