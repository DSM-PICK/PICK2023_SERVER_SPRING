package com.pickdsm.pickserverspring.common.feign.client

import com.pickdsm.pickserverspring.common.feign.client.dto.response.UserInfoResponse
import com.pickdsm.pickserverspring.common.feign.client.dto.response.UserInfoResponse.UserInfoElement
import com.pickdsm.pickserverspring.common.feign.client.dto.response.UserResponse
import com.pickdsm.pickserverspring.domain.user.dto.request.UserInfoRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID
import javax.validation.Valid

@FeignClient(name = "UserClient", url = "\${service.scheme}://\${service.user.host}")
interface UserClient {

    @PostMapping("/users/id")
    fun getUserInfo(@Valid @RequestBody request: UserInfoRequest): UserInfoResponse

    @GetMapping("/users/class")
    fun getUserInfoByGradeAndClassNum(
        @RequestParam("grade") grade: Int?,
        @RequestParam("classNum") classNum: Int?,
    ): UserResponse

    @GetMapping("/users/id/{userId}")
    fun getUserInfoByUserId(@PathVariable("userId") userId: UUID): UserInfoElement

    @GetMapping("/users/all")
    fun getAllUserInfo(): UserInfoResponse
}
