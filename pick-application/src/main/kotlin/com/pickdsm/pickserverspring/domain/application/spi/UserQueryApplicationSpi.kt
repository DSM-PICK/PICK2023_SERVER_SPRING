package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.user.User
import java.util.*

interface UserQueryApplicationSpi {

    fun queryUserInfo(ids: List<UUID>): List<User>
}
