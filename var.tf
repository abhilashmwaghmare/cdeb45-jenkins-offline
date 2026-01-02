variable "aws_region" {
  default = "ap-south-1"
}

variable "cluster_name" {
  default = "demo-eks-cluster1"
}

variable "node_instance_type" {
  default = "c7i.flex-large"
}

variable "desired_nodes" {
  default = 2
}

variable "key_pair_name" {
  description = "EC2 Key Pair name for SSH access to nodes (optional)"
  type        = string
  default     = ""
}


