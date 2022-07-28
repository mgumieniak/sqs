variable "aws_account_number" {
  default = "056206897674"
}

variable "aws_account_client" {
  default = "webapp_clients"
}

variable "aws_account_client_role" {
  default = "webapp_client_role"
}

# SQS
variable "sqs_name" {
  default = "sqs_mgumieniak"
}

variable "sqs_dlq_name" {
  default = "sqs_mgumieniak_dlq"
}

# SQS FIFO
variable "sqs_fifo_name" {
  default = "sqs_mgumieniak.fifo"
}

variable "sqs_fifo_dlq_name" {
  default = "sqs_mgumieniak_dlq.fifo"
}
