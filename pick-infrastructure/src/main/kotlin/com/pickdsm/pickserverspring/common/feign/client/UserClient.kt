package com.pickdsm.pickserverspring.common.feign.client

import com.pickdsm.pickserverspring.common.feign.client.dto.response.UserInfoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@FeignClient(name = "UserClient", url = "\${service.scheme}://\${service.user.host}")
interface UserClient {

    @GetMapping("/id")
    fun getUserInfo(@RequestParam("userId") ids: List<UUID>): UserInfoResponse
}
