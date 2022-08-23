package com.mgumieniak.webapp

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsAsyncClient
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest


@TestConfiguration
class SNSConfig {
    companion object {
        private const val ACCOUNT_NB = "056206897674"
        const val USER = "webapp_client"
        const val ROLE = "webapp_client_role"
        const val REGION = "eu-west-1"
        private const val SNS_TOPIC_NAME = "demo-topic"

        const val TOPIC_ARN = "arn:aws:sns:${REGION}:$ACCOUNT_NB:$SNS_TOPIC_NAME"
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
    fun snsClient(credentialsProvider: AwsCredentialsProvider): SnsAsyncClient = SnsAsyncClient.builder()
        .credentialsProvider(credentialsProvider)
        .region(Region.of(REGION))
        .build()
}
