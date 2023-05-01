package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.dto.request.UserInfoRequest
import java.util.UUID

interface UserQueryApplicationSpi {

    fun queryUserInfo(request: UserInfoRequest): List<User>

    fun queryUserInfoByUserId(userId: UUID): User
}
