package com.mgumieniak.webapp.sns

import com.mgumieniak.webapp.SNSConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import software.amazon.awssdk.services.sns.SnsAsyncClient
import software.amazon.awssdk.services.sns.model.PublishBatchRequest
import software.amazon.awssdk.services.sns.model.PublishBatchRequestEntry
import java.util.UUID

@SpringBootTest(classes = [SNSConfig::class])
class SNSTest {

    @Autowired
    lateinit var snsClient: SnsAsyncClient

    @Test
    fun `sends message`() {
        val batchRequest = PublishBatchRequest.builder()
            .topicArn(SNSConfig.TOPIC_ARN)
            .publishBatchRequestEntries(
                setOf(
                    PublishBatchRequestEntry.builder()
                        .id(UUID.randomUUID().toString())
                        .message("Jeż 1")
                        .build(),
                    PublishBatchRequestEntry.builder()
                        .id(UUID.randomUUID().toString())
                        .message("Jeż 2")
                        .build()
                )
            )
            .build()

        snsClient.publishBatch(batchRequest).get()
    }
}
