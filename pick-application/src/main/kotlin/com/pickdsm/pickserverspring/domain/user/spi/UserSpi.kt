package com.pickdsm.pickserverspring.domain.user.spi

import com.pickdsm.pickserverspring.domain.application.spi.UserQueryApplicationSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.UserQuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.UserQueryTeacherSpi
import java.util.*

interface UserSpi : UserQueryApplicationSpi, UserQuerySelfStudyDirectorSpi, UserQueryTeacherSpi {
    fun getCurrentUserId(): UUID
}
