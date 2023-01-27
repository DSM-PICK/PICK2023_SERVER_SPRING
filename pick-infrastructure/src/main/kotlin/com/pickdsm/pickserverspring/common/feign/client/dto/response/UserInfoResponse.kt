package com.pickdsm.pickserverspring.common.feign.client.dto.response

import com.pickdsm.pickserverspring.global.annotation.NoArg

@NoArg
class UserInfoResponse(
    val users: List<UserInfoElement>,
)
