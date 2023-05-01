package com.pickdsm.pickserverspring.domain.selfstudydirector.spi

import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.dto.request.UserInfoRequest
import java.util.UUID

interface UserQuerySelfStudyDirectorSpi {

    fun queryUserInfo(request: UserInfoRequest): List<User>
}
