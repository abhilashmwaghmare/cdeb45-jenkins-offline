output "cluster_name" {
  value = aws_eks_cluster.eks.name
}

output "cluster_endpoint" {
  value = aws_eks_cluster.eks.endpoint
}

output "cluster_security_group_id" {
  # vpc_config is a single nested object, accessed directly without index
  value = aws_eks_cluster.eks.vpc_config.cluster_security_group_id
}

output "node_group_arn" {
  value = aws_eks_node_group.nodes.arn
}

output "cluster_version" {
  value = aws_eks_cluster.eks.version
}