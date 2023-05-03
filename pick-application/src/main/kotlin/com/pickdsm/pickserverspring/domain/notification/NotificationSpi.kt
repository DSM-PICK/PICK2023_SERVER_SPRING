package com.pickdsm.pickserverspring.domain.notification

import java.util.UUID

interface NotificationSpi {
    fun sendNotification(userId: UUID, topic: String, content: String, threadId: String)

    fun sendGroupNotification(topic: String, content: String, threadId: String)
}
