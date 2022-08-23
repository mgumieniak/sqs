variable "aws_account_number" {}
variable "aws_account_client" {}
variable "aws_account_client_role" {}
variable "sns_name" { default = "sns_demo" }

resource "aws_sns_topic" "demo-topic" {
  name = "demo-topic"
}

resource "aws_sns_topic_policy" "sqs_client_policy" {
  arn = aws_sns_topic.demo-topic.arn

  policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Sid" : "SNS permissions for producer",
        "Effect" : "Allow",
        "Principal" : {
          "AWS" : "arn:aws:iam::${var.aws_account_number}:role/${var.aws_account_client_role}"
        },
        "Action" : [
          "SNS:Publish",
        ],
        "Resource" : aws_sns_topic.demo-topic.arn,
        "Condition": {
          "Bool": {
            "aws:SecureTransport": "true"
          }
        }
      }
    ]
  })
}
