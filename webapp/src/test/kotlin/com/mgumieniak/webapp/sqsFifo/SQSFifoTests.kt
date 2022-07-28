package com.mgumieniak.webapp.sqsFifo

import com.mgumieniak.webapp.SQSConfig
import com.mgumieniak.webapp.SQSConfig.Companion.SQS_FIFO_URL
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.*

@SpringBootTest(classes = [SQSConfig::class])
class SQSFifoTests {

    @Autowired
    lateinit var sqsClient: SqsAsyncClient

    @Test
    fun `deduplicates incoming messages`() {
        val messages = SendMessageBatchRequest.builder()
            .queueUrl(SQS_FIFO_URL)
            .entries(
                // Deduplication based on body hash
                message(id = "1", groupId = "g1", deduplicationId = null, body = "Hello World!"),
                message(id = "2", groupId = "g1", deduplicationId = null, body = "Hello World!"),

                // Deduplication based on deduplicationId (hash is ignored)
                message(id = "3", groupId = "g1", deduplicationId = "dedId_1", body = "Hello World!"),
                message(id = "4", groupId = "g1", deduplicationId = "dedId_1", body = "Hello World!"),
            )
            .build()
        sqsClient.sendMessageBatch(messages).get()

        // Receive messages
        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl(SQS_FIFO_URL)
            .maxNumberOfMessages(10)
            .attributeNamesWithStrings("MessageDeduplicationId")
            .build()
        val returnedMessages = sqsClient.receiveMessage(receiveMessageRequest).get()

        // Delete processed messages
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

        assertThat(returnedMessages.messages().map { it.attributesAsStrings()["MessageDeduplicationId"] })
            .containsOnlyOnce("7f83b1657ff1fc53b92dc18148a1d65dfc2d4b1fa3d677284addd200126d9069", "dedId_1")
    }

    private fun message(id: String, groupId: String, deduplicationId: String? = null, body: String) =
        SendMessageBatchRequestEntry.builder()
            .id(id)
            .messageGroupId(groupId)
            .messageDeduplicationId(deduplicationId)
            .messageBody(body)
            .build()
}
