package com.pickdsm.pickserverspring.domain.selfstudydirector.spi

import com.pickdsm.pickserverspring.domain.user.User
import java.util.UUID

interface UserQuerySelfStudyDirectorSpi {

    fun queryUserInfo(ids: List<UUID>): List<User>
}
