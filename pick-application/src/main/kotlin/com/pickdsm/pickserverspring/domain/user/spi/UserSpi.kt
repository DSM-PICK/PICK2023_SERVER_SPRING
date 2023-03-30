package com.pickdsm.pickserverspring.domain.user.spi

import com.pickdsm.pickserverspring.domain.application.spi.UserQueryApplicationSpi
import com.pickdsm.pickserverspring.domain.selfstudydirector.spi.UserQuerySelfStudyDirectorSpi
import com.pickdsm.pickserverspring.domain.teacher.spi.UserQueryTeacherSpi
import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.dto.UserInfo
import java.util.UUID

interface UserSpi : UserQueryApplicationSpi, UserQuerySelfStudyDirectorSpi, UserQueryTeacherSpi {
    fun getCurrentUserId(): UUID

    fun getAllUserInfo(): List<User>
}
