package com.pickdsm.pickserverspring.common.feign.client

import com.pickdsm.pickserverspring.common.feign.client.dto.response.UserInfoResponse
import com.pickdsm.pickserverspring.common.feign.client.dto.response.UserInfoResponse.UserInfoElement
import com.pickdsm.pickserverspring.common.feign.client.dto.response.UserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@FeignClient(name = "UserClient", url = "\${service.scheme}://\${service.user.host}")
interface UserClient {

    @GetMapping("/users/id")
    fun getUserInfo(@RequestParam("userId") ids: List<UUID>): UserInfoResponse

    @GetMapping("/users/class")
    fun getUserInfoByGradeAndClassNum(
        @RequestParam("grade") grade: Int?,
        @RequestParam("classNum") classNum: Int?,
    ): UserResponse

    @GetMapping("/users/id/{userId}")
    fun getUserInfoByUserId(@PathVariable("userId") userId: UUID): UserInfoElement
}
