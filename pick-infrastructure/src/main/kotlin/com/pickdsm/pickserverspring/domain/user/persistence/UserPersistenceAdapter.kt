package com.pickdsm.pickserverspring.domain.user.persistence

import com.pickdsm.pickserverspring.common.feign.client.UserClient
import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Adapter
class UserPersistenceAdapter(
    private val userClient: UserClient,
) : UserSpi {

    override fun getCurrentUserId(): UUID =
        UUID.fromString(SecurityContextHolder.getContext().authentication.name)

    override fun queryUserInfo(ids: List<UUID>): List<User> =
        userClient.getUserInfo(ids)
            .users
            .map {
                User(
                    id = it.id,
                    accountId = it.accountId,
                    password = it.password,
                    name = it.name,
                    grade = it.grade,
                    classNum = it.classNum,
                    num = it.num,
                    birthDay = it.birthDay,
                    profileFileName = it.profileFileName,
                )
            }
}
