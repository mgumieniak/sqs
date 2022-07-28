package com.mgumieniak.webapp.sqs

import com.mgumieniak.webapp.SQSConfig
import com.mgumieniak.webapp.SQSConfig.Companion.SQS_URL
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.*
import java.util.*

@SpringBootTest(classes = [SQSConfig::class])
class SQSTests {

    val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var sqsClient: SqsAsyncClient

    @Test
    fun `sends message`() {
        val msg = SendMessageRequest.builder()
            .queueUrl(SQS_URL)
            .messageBody("Hello World")
            .messageAttributes(
                mapOf(
                    Pair(
                        "id",
                        MessageAttributeValue.builder()
                            .stringValue(UUID.randomUUID().toString())
                            .dataType("String")
                            .build()
                    )
                )
            )
            .build()

        sqsClient.sendMessage(msg).get()
    }

    @Test
    fun `receives msg`() {
        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl(SQS_URL)
            .maxNumberOfMessages(10)
            .build()

        val returnedMessages = sqsClient.receiveMessage(receiveMessageRequest).get()

        logger.info(returnedMessages.toString())

        val messagesIdsToDelete = returnedMessages.messages().map { it.messageId() }
        sqsClient.deleteMessageBatch(
            DeleteMessageBatchRequest.builder()
                .entries(messagesIdsToDelete.map { messageIdToDelete ->
                    DeleteMessageBatchRequestEntry.builder()
                        .id(messageIdToDelete)
                        .build()
                })
                .build()
        )
    }
}
