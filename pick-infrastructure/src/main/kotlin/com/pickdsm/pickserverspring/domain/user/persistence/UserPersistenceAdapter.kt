package com.pickdsm.pickserverspring.domain.user.persistence

import com.pickdsm.pickserverspring.common.feign.client.UserClient
import com.pickdsm.pickserverspring.domain.user.dto.UserInfo
import com.pickdsm.pickserverspring.domain.user.spi.ApplicationUserSpi
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Adapter
class UserPersistenceAdapter(
    private val userClient: UserClient,
) : UserSpi, ApplicationUserSpi {

    override fun getCurrentUserId(): UUID =
        UUID.fromString(SecurityContextHolder.getContext().authentication.name)

    override fun queryUserInfo(ids: List<UUID>): List<UserInfo> {
        val userList = userClient.getUserInfo(ids).userList

        return userList
            .map {
                UserInfo(
                    id = it.id,
                    grade = it.grade,
                    classNum = it.classNum,
                    num = it.num,
                    studentName = it.name,
                )
            }
    }
}
