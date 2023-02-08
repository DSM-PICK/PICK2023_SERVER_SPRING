package com.pickdsm.pickserverspring.domain.teacher.spi

import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.dto.UserInfo
import java.util.UUID

interface UserQueryTeacherSpi {

    fun queryUserInfo(ids: List<UUID>): List<User>

    fun queryUserInfoByGradeAndClassNum(grade: Int, classNum: Int): List<UserInfo>
}
