package com.pickdsm.pickserverspring.domain.notification

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.MessageAttributeValue
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.pickdsm.pickserverspring.domain.notification.dto.request.DomainSendGroupMessageRequest
import com.pickdsm.pickserverspring.domain.notification.dto.request.DomainSendMessageRequest
import com.pickdsm.pickserverspring.global.annotation.Adapter
import java.util.UUID

@Adapter
class NotificationAdapter(
    private val objectMapper: ObjectMapper,
    private val amazonSQS: AmazonSQS,
) : NotificationSpi {

    companion object {
        const val NOTIFICATION_FIFO = "notification.fifo"
        const val NOTIFICATION_GROUP_FIFO = "notification-group.fifo"
    }

    override fun sendNotification(userId: UUID, topic: String, content: String, threadId: String) {
        val domainSendMessageRequest = DomainSendMessageRequest(
            userId = userId,
            topic = topic,
            content = content,
            threadId = threadId,
        )

        sendSqsMessage(NOTIFICATION_FIFO, objectMapper.writeValueAsString(domainSendMessageRequest))
    }

    override fun sendGroupNotification(topic: String, content: String, threadId: String) {
        val domainSendGroupMessageRequest = DomainSendGroupMessageRequest(
            topic = topic,
            content = content,
            threadId = threadId,
        )

        sendSqsMessage(NOTIFICATION_GROUP_FIFO, objectMapper.writeValueAsString(domainSendGroupMessageRequest))
    }

    private fun sendSqsMessage(queueUrl: String, content: String) {
        val sendMessageRequest = SendMessageRequest(
            amazonSQS.getQueueUrl(queueUrl).queueUrl,
            content,
        )
            .withMessageGroupId("pick")
            .withMessageDeduplicationId(UUID.randomUUID().toString())
            .withMessageAttributes(
                mapOf(
                    "Content-Type" to MessageAttributeValue()
                        .withDataType("String")
                        .withStringValue("application/json"),
                ),
            )
        amazonSQS.sendMessage(sendMessageRequest)
    }
}
