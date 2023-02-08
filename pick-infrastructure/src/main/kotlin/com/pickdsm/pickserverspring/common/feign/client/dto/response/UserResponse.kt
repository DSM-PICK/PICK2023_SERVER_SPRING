package com.pickdsm.pickserverspring.common.feign.client.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class UserResponse(
    val response: List<UserElement>,
) {

    data class UserElement(
        val id: UUID,
        @JsonProperty("profile_file_name")
        val profileFileName: String,
        val num: Int,
        val name: String,
    )
}
