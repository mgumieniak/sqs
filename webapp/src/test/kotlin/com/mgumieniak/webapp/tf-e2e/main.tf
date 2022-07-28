#module "sqs" {
#  source = "../sqs"
#
#  aws_account_number      = var.aws_account_number
#  aws_account_client      = var.aws_account_client
#  aws_account_client_role = var.aws_account_client_role
#
#  sqs_name     = var.sqs_name
#  sqs_dlq_name = var.sqs_dlq_name
#}

module "sqs_dlq" {
  source = "../sqsFifo"

  aws_account_number      = var.aws_account_number
  aws_account_client      = var.aws_account_client
  aws_account_client_role = var.aws_account_client_role

  sqs_fifo_name = var.sqs_fifo_name
  sqs_fifo_dlq_name = var.sqs_fifo_dlq_name
}
