package com.mgumieniak.webapp

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest


@TestConfiguration
class SQSConfig {
    companion object {
        private const val ACCOUNT_NB = "056206897674"
        const val USER = "webapp_client"
        const val ROLE = "webapp_client_role"
        const val REGION = "eu-west-1"
        private const val SQS_NAME = "sqs_mgumieniak"
        private const val SQS_FIFO_NAME = "sqs_mgumieniak.fifo"

        const val SQS_FIFO_URL = "https://sqs.${REGION}.amazonaws.com/$ACCOUNT_NB/$SQS_FIFO_NAME"
        const val SQS_URL = "https://sqs.${REGION}.amazonaws.com/$ACCOUNT_NB/$SQS_NAME"
        const val ROLE_ARN = "arn:aws:iam::$ACCOUNT_NB:role/$ROLE"
        const val TMP_CRED_EXP_IN_SEC = 1500
    }

    @Bean
    fun stsClient(): StsClient = StsClient.builder()
        .region(Region.of(REGION))
        .credentialsProvider(ProfileCredentialsProvider.create(USER))
        .build()

    @Bean
    fun stsAssumeRoleCredentialsProvider(stsClient: StsClient): StsAssumeRoleCredentialsProvider {
        val roleRequest = AssumeRoleRequest.builder()
            .roleArn(ROLE_ARN)
            .roleSessionName(ROLE)
            .durationSeconds(TMP_CRED_EXP_IN_SEC)
            .build()

        return StsAssumeRoleCredentialsProvider.builder()
            .stsClient(stsClient)
            .refreshRequest(roleRequest)
            .build()
    }

    @Bean
    fun sqsClient(credentialsProvider: AwsCredentialsProvider): SqsAsyncClient = SqsAsyncClient.builder()
        .credentialsProvider(credentialsProvider)
        .region(Region.of(REGION))
        .build()
}
