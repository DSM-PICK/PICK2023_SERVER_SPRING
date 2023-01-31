package com.pickdsm.pickserverspring.domain.teacher.spi

import com.pickdsm.pickserverspring.domain.user.User
import java.util.*

interface UserQueryTeacherSpi {

    fun queryUserInfo(ids: List<UUID>): List<User>
}