package com.pickdsm.pickserverspring.domain.user.persistence

import com.pickdsm.pickserverspring.common.feign.client.UserClient
import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.dto.UserInfo
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
        if (ids.isEmpty()) {
            emptyList()
        } else {
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

    override fun queryUserInfoByGradeAndClassNum(grade: Int?, classNum: Int?): List<UserInfo> =
        userClient.getUserInfoByGradeAndClassNum(grade, classNum)
            .response
            .map {
                UserInfo(
                    id = it.id,
                    profileFileName = it.profileFileName,
                    num = it.num,
                    name = it.name,
                )
            }

    override fun queryUserInfoByUserId(userId: UUID): User {
        val user = userClient.getUserInfoByUserId(userId)

        return User(
                id = user.id,
                accountId = user.accountId,
                password = user.password,
                name = user.name,
                grade = user.grade,
                classNum = user.classNum,
                num = user.num,
                birthDay = user.birthDay,
                profileFileName = user.profileFileName,
        )
    }
}
