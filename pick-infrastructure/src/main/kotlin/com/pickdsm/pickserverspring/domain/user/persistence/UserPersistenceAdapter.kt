package com.pickdsm.pickserverspring.domain.user.persistence

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pickdsm.pickserverspring.common.feign.client.UserClient
import com.pickdsm.pickserverspring.common.feign.client.dto.response.UserInfoResponse
import com.pickdsm.pickserverspring.domain.user.dto.UserInfo
import com.pickdsm.pickserverspring.domain.user.spi.ApplicationUserSpi
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

@Adapter
class UserPersistenceAdapter(
    private val userClient: UserClient,
) : UserSpi, ApplicationUserSpi {

    override fun getCurrentUserId(): UUID =
        UUID.fromString(SecurityContextHolder.getContext().authentication.name)

    override fun queryUserInfo(ids: List<UUID>): List<UserInfo> =
        jacksonObjectMapper().readValue<UserInfoResponse>(userClient.getUserInfo(ids))
            .userList
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
