package com.pickdsm.pickserverspring.domain.user.spi

import com.pickdsm.pickserverspring.domain.application.spi.UserQueryApplicationSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.UserQuerySelfStudyDirectorSpi
import java.util.*

interface UserSpi : UserQueryApplicationSpi, UserQuerySelfStudyDirectorSpi {
    fun getCurrentUserId(): UUID
}
