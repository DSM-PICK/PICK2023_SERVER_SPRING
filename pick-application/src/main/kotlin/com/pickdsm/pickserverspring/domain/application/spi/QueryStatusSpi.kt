package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Status
import com.pickdsm.pickserverspring.domain.application.StatusType
import com.pickdsm.pickserverspring.domain.application.vo.PicnicApplicationVO
import java.time.LocalDate
import java.util.UUID

interface QueryStatusSpi {

    fun queryPicnicStudentInfoListByToday(date: LocalDate): List<Status>

    fun queryMovementStudentInfoListByToday(date: LocalDate): List<Status>

    fun queryAwaitStudentListByToday(): List<Status>

    fun queryStatusListByToday(): List<Status>

    fun queryStatusListByDate(date: LocalDate): List<Status>

    fun queryPicnicStudentByStudentIdAndToday(studentId: UUID): Status?

    fun queryStatusByStudentIdAndStartPeriodAndEndPeriodAndToday(
        studentId: UUID,
        startPeriod: Int,
        endPeriod: Int,
    ): Status?

    fun queryMovementStudentByStudentId(studentId: UUID): Status?

    fun queryMovementStudentStatusIdByStudentIdAndToday(studentId: UUID): UUID?

    fun queryStatusTypesByStudentIdAndEndPeriodAndToday(studentId: UUID, period: Int): List<StatusType>

    fun queryMovementStatusListByTodayAndClassroomId(classroomId: UUID): List<Status>

    fun queryPicnicApplicationStatusSizeByToday(): Int

    fun queryMovementStatusSizeByFloorAndToday(floor: Int): Int

    fun queryPicnicStatusSizeByToday(): Int

    fun existAwaitOrPicnicStatusByStudentId(studentId: UUID): Boolean

    fun queryPicnicOrAwaitOrMovementStatusStudentIdListByToday(): List<UUID>

    fun queryPicnicApplicationListByToday(): List<PicnicApplicationVO>
}
