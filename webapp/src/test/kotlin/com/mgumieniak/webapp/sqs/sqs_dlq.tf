variable "sqs_dlq_name" {}


resource "aws_sqs_queue" "sqs_dlq" {
  name                      = var.sqs_dlq_name
  delay_seconds             = 0
  max_message_size          = 262144 # 256 KiB
  message_retention_seconds = 1209600 # 14 days

  # set the visibility timeout to the maximum time that it takes your application to process and delete a message from the queue
  visibility_timeout_seconds = 5
  receive_wait_time_seconds  = 20 # 20s long polling - be aware of the timeout!

  kms_master_key_id                 = aws_kms_key.mgumieniak_sqs_key.key_id
  kms_data_key_reuse_period_seconds = 86400

  tags = {
    environment = "dev"
  }
}
