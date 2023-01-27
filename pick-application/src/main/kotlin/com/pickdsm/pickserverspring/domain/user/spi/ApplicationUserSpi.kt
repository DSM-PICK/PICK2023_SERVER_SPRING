package com.pickdsm.pickserverspring.domain.user.spi

import com.pickdsm.pickserverspring.domain.user.dto.UserInfo
import java.util.UUID

interface ApplicationUserSpi {

    fun queryUserInfo(ids: List<UUID>): List<UserInfo>
}
