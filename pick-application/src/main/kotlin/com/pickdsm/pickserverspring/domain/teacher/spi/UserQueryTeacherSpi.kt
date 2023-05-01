package com.pickdsm.pickserverspring.domain.teacher.spi

import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.dto.UserInfo
import com.pickdsm.pickserverspring.domain.user.dto.request.UserInfoRequest
import java.util.UUID

interface UserQueryTeacherSpi {

    fun queryUserInfo(request: UserInfoRequest): List<User>

    fun queryUserInfoByGradeAndClassNum(grade: Int?, classNum: Int?): List<UserInfo>
}
