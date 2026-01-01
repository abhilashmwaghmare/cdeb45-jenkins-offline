# 🖥️ Jenkins Server Setup & Configuration Guide

<div align="center">

![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white)
![Terraform](https://img.shields.io/badge/terraform-7B42BC?style=for-the-badge&logo=terraform&logoColor=white)
![Kubernetes](https://img.shields.io/badge/kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white)

**Complete guide to set up Jenkins server with all required tools for CI/CD pipelines**

</div>

---

## 📋 Table of Contents

- [Prerequisites](#-prerequisites)
- [Initial Server Setup](#-initial-server-setup)
- [Install Terraform](#-install-terraform)
- [Install kubectl](#-install-kubectl)
- [Install AWS CLI](#-install-aws-cli)
- [Install Docker](#-install-docker)
- [Configure Jenkins for Docker](#-configure-jenkins-for-docker)
- [Configure AWS Access](#-configure-aws-access)
- [Configure EKS Access](#-configure-eks-access)
- [Verify Installation](#-verify-installation)
- [Troubleshooting](#-troubleshooting)

---

## 🎯 Prerequisites

Before starting, ensure you have:

- ✅ Ubuntu/Debian Linux server (EC2 instance recommended)
- ✅ SSH access to the server
- ✅ Sudo/root privileges
- ✅ AWS Account (for EKS deployment)
- ✅ Docker Hub account (for container registry)

---

## 🚀 Initial Server Setup

### Update System Packages

```bash
# Update package list
sudo apt update -y

# Upgrade existing packages
sudo apt upgrade -y
```

---

## 🏗️ Install Terraform

Terraform is required for Infrastructure as Code (IaC) and EKS cluster provisioning.

### Installation Steps

```bash
# Install prerequisites
sudo apt update && \
sudo apt install -y gnupg software-properties-common curl

# Add HashiCorp GPG key
curl -fsSL https://apt.releases.hashicorp.com/gpg | \
    sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg

# Add HashiCorp repository
echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] \
    https://apt.releases.hashicorp.com $(lsb_release -cs) main" | \
    sudo tee /etc/apt/sources.list.d/hashicorp.list

# Update package list
sudo apt update

# Install Terraform
sudo apt install -y terraform

# Verify installation
terraform version
```

**Expected Output:**
```
Terraform v1.6.x
```

### Verify Terraform Works

```bash
# Test Terraform
terraform --help
```

---

## ☸️ Install kubectl

kubectl is the Kubernetes command-line tool for managing Kubernetes clusters.

### Installation Steps

```bash
# Download latest kubectl
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"

# Make executable
chmod +x kubectl

# Move to system path
sudo mv kubectl /usr/local/bin/

# Verify installation
kubectl version --client
```

**Expected Output:**
```
Client Version: version.Info{Major:"1", Minor:"28", ...}
```

### Verify kubectl Works

```bash
# Test kubectl
kubectl --help
```

---

## ☁️ Install AWS CLI

AWS CLI is required for interacting with AWS services and managing EKS clusters.

### Installation Steps

```bash
# Download AWS CLI v2
curl -O "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip"

# Rename for clarity (optional)
mv awscli-exe-linux-x86_64.zip awscliv2.zip

# Install unzip if not already installed
sudo apt install -y unzip

# Extract AWS CLI
unzip awscliv2.zip

# Install AWS CLI
sudo ./aws/install

# Verify installation
aws --version
```

**Expected Output:**
```
aws-cli/2.x.x Python/3.x.x Linux/x.x.x
```

### Alternative: Install via Package Manager

```bash
# For older systems or if above method fails
sudo apt install awscli -y
aws --version
```

---

## 🐳 Install Docker

Docker is required for building container images and running containerized applications.

### Installation Steps

```bash
# Update package list
sudo apt update -y

# Install Docker
sudo apt install docker.io -y

# Start Docker service
sudo systemctl start docker
sudo systemctl enable docker

# Verify installation
docker --version
```

**Expected Output:**
```
Docker version 20.10.x, build xxxxx
```

### Verify Docker Works

```bash
# Test Docker
sudo docker run hello-world
```

**Expected Output:**
```
Hello from Docker!
This message shows that your installation appears to be working correctly.
```

---

## 🔧 Configure Jenkins for Docker

Jenkins needs permission to use Docker without sudo.

### Step 1: Add Jenkins User to Docker Group

```bash
# Add jenkins user to docker group
sudo gpasswd -a jenkins docker

# Verify jenkins is in docker group
groups jenkins
```

### Step 2: Restart Services

```bash
# Restart Docker
sudo systemctl restart docker

# Restart Jenkins
sudo systemctl restart jenkins
```

### Step 3: Verify Docker Access (as Jenkins User)

```bash
# Switch to jenkins user
sudo su - jenkins

# Test Docker (should work without sudo)
docker ps

# Exit jenkins user
exit
```

> ⚠️ **Note**: If `docker ps` fails, you may need to log out and log back in, or restart the server.

---

## 🔐 Configure AWS Access

Jenkins needs AWS credentials to interact with AWS services.

### Method 1: AWS Configure (Recommended for Development)

```bash
# Switch to jenkins user
sudo su - jenkins

# Configure AWS CLI
aws configure
```

**You'll be prompted for:**
```
AWS Access Key ID [None]: YOUR_ACCESS_KEY
AWS Secret Access Key [None]: YOUR_SECRET_KEY
Default region name [None]: ap-south-1
Default output format [None]: json
```

### Method 2: Environment Variables

```bash
# Add to jenkins user's .bashrc
sudo su - jenkins
echo 'export AWS_ACCESS_KEY_ID="YOUR_ACCESS_KEY"' >> ~/.bashrc
echo 'export AWS_SECRET_ACCESS_KEY="YOUR_SECRET_KEY"' >> ~/.bashrc
echo 'export AWS_DEFAULT_REGION="ap-south-1"' >> ~/.bashrc
source ~/.bashrc
```

### Method 3: IAM Role (Recommended for Production)

If Jenkins is running on an EC2 instance:

1. Create an IAM role with necessary permissions
2. Attach the role to the EC2 instance
3. Jenkins will automatically use the instance role

**Required IAM Permissions:**
- `AmazonEKSClusterPolicy`
- `AmazonEKSWorkerNodePolicy`
- `AmazonEC2ContainerRegistryReadOnly`
- `AmazonS3FullAccess` (if using S3)

### Verify AWS Configuration

```bash
# Test AWS CLI
aws sts get-caller-identity
```

**Expected Output:**
```json
{
    "UserId": "AIDA...",
    "Account": "123456789012",
    "Arn": "arn:aws:iam::123456789012:user/jenkins"
}
```

---

## ☸️ Configure EKS Access

Configure kubectl to connect to your EKS cluster.

### Step 1: Update kubeconfig

```bash
# Switch to jenkins user
sudo su - jenkins

# Update kubeconfig for EKS cluster
aws eks update-kubeconfig \
    --region ap-south-1 \
    --name <EKS_CLUSTER_NAME>
```

**Replace:**
- `ap-south-1` with your AWS region
- `<EKS_CLUSTER_NAME>` with your EKS cluster name

**Example:**
```bash
aws eks update-kubeconfig \
    --region ap-south-1 \
    --name my-eks-cluster
```

### Step 2: Verify EKS Connection

```bash
# Check cluster connection
kubectl get nodes
```

**Expected Output:**
```
NAME                                          STATUS   ROLES    AGE   VERSION
ip-10-0-1-xxx.ap-south-1.compute.internal   Ready    <none>   5d    v1.28.x
```

### Step 3: Verify Services

```bash
# Check running services
kubectl get svc

# Check all resources
kubectl get all
```

---

## 🐳 Configure Docker Hub Access

For pushing Docker images to Docker Hub.

### Step 1: Login to Docker Hub

```bash
# Switch to jenkins user
sudo su - jenkins

# Login to Docker Hub
docker login -u "your-username" -p "your-token"
```

> 💡 **Tip**: Use a Docker Hub access token instead of password for better security.

### Step 2: Verify Login

```bash
# Test Docker Hub access
docker pull hello-world
docker push your-username/test-image:latest
```

---

## ✅ Verify Installation

Run this comprehensive verification script:

```bash
#!/bin/bash

echo "=== Verification Script ==="
echo ""

echo "1. Terraform:"
terraform version
echo ""

echo "2. kubectl:"
kubectl version --client
echo ""

echo "3. AWS CLI:"
aws --version
echo ""

echo "4. Docker:"
docker --version
echo ""

echo "5. AWS Identity:"
aws sts get-caller-identity
echo ""

echo "6. Kubernetes Nodes:"
kubectl get nodes
echo ""

echo "7. Docker Access (as jenkins user):"
sudo -u jenkins docker ps
echo ""

echo "=== Verification Complete ==="
```

**Save and run:**
```bash
chmod +x verify-setup.sh
./verify-setup.sh
```

---

## 🐛 Troubleshooting

### Issue 1: Terraform Not Found

**Symptoms**: `terraform: command not found`

**Solution:**
```bash
# Verify installation
which terraform

# Reinstall if needed
sudo apt install terraform -y
```

---

### Issue 2: kubectl Permission Denied

**Symptoms**: `kubectl: permission denied`

**Solution:**
```bash
# Check permissions
ls -la /usr/local/bin/kubectl

# Fix permissions
sudo chmod +x /usr/local/bin/kubectl
```

---

### Issue 3: AWS CLI Not Working

**Symptoms**: `Unable to locate credentials`

**Solution:**
```bash
# Reconfigure AWS
aws configure

# Or check credentials file
cat ~/.aws/credentials
```

---

### Issue 4: Docker Permission Denied

**Symptoms**: `permission denied while trying to connect to the Docker daemon socket`

**Solution:**
```bash
# Add user to docker group
sudo gpasswd -a jenkins docker

# Restart services
sudo systemctl restart docker
sudo systemctl restart jenkins

# Log out and log back in, or restart server
```

---

### Issue 5: kubectl Cannot Connect to EKS

**Symptoms**: `Unable to connect to the server`

**Solution:**
```bash
# Verify AWS credentials
aws sts get-caller-identity

# Reconfigure kubeconfig
aws eks update-kubeconfig --region ap-south-1 --name <CLUSTER_NAME>

# Check cluster status
aws eks describe-cluster --name <CLUSTER_NAME> --region ap-south-1
```

---

### Issue 6: Jenkins Cannot Access Docker

**Symptoms**: Jenkins pipeline fails with Docker errors

**Solution:**
```bash
# Verify jenkins user in docker group
groups jenkins

# Add if missing
sudo gpasswd -a jenkins docker

# Restart Jenkins
sudo systemctl restart jenkins

# Test as jenkins user
sudo -u jenkins docker ps
```

---

## 📋 Quick Reference

### Installation Commands Summary

```bash
# Terraform
sudo apt install terraform -y

# kubectl
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x kubectl && sudo mv kubectl /usr/local/bin/

# AWS CLI
curl -O "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip"
unzip awscliv2.zip && sudo ./aws/install

# Docker
sudo apt install docker.io -y
sudo systemctl start docker && sudo systemctl enable docker

# Configure Jenkins for Docker
sudo gpasswd -a jenkins docker
sudo systemctl restart docker && sudo systemctl restart jenkins
```

### Configuration Commands Summary

```bash
# AWS Configuration
aws configure

# EKS Configuration
aws eks update-kubeconfig --region <REGION> --name <CLUSTER_NAME>

# Docker Hub Login
docker login -u <USERNAME> -p <TOKEN>
```

---

## 🔒 Security Best Practices

### 1. Use IAM Roles (Production)

✅ **Do**: Use IAM roles for EC2 instances  
❌ **Don't**: Store AWS credentials in files

### 2. Use Docker Hub Tokens

✅ **Do**: Use access tokens instead of passwords  
❌ **Don't**: Use plain text passwords

### 3. Limit Permissions

✅ **Do**: Grant minimum required permissions  
❌ **Don't**: Use admin/root access unnecessarily

### 4. Regular Updates

✅ **Do**: Keep all tools updated  
❌ **Don't**: Use outdated versions

### 5. Secure Credentials

✅ **Do**: Use Jenkins credential store  
❌ **Don't**: Hardcode credentials in pipelines

---

## 📚 Next Steps

After completing this setup:

1. ✅ **Create Your First Pipeline** - See [Jenkins Guide](jenkins.md)
2. ✅ **Set Up SonarQube** - See [SonarQube Installation](sonarqube-installation.md)
3. ✅ **Deploy to EKS** - See [EKS Pipeline Examples](README.md#-pipeline-examples)
4. ✅ **Configure Monitoring** - Set up CloudWatch, Prometheus, etc.

---

<div align="center">

**Setup Complete! 🎉**

[← Back to README](README.md) • [Jenkins Guide →](jenkins.md)

</div>

