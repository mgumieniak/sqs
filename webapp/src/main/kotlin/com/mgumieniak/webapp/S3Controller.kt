package com.mgumieniak.webapp

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.Bucket
import software.amazon.awssdk.services.s3.model.ListBucketsRequest


@RestController
@RequestMapping("/s3")
class S3Controller {

    @GetMapping("/images")
    fun get() {
        val region: Region = Region.EU_WEST_1
        val s3: S3Client = S3Client.builder()
            .region(region)
            .build()

        val listBucketsRequest = ListBucketsRequest.builder().build()
        val listBucketsResponse = s3.listBuckets(listBucketsRequest)
        listBucketsResponse.buckets().stream().forEach { x: Bucket ->
            println(
                x.name()
            )
        }
    }
}
