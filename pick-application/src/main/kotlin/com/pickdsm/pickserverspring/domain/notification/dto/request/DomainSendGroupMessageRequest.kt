package com.pickdsm.pickserverspring.domain.notification.dto.request

data class DomainSendGroupMessageRequest(
    val topic: String,
    val content: String,
    val threadId: String,
)
