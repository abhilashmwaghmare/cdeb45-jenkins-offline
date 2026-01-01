# 🚀 Jenkins CI/CD Pipeline with Maven, SonarQube, and EKS

<div align="center">

![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white)
![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-4E9BCD?style=for-the-badge&logo=SonarQube&logoColor=white)
![Kubernetes](https://img.shields.io/badge/kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white)
![Terraform](https://img.shields.io/badge/terraform-7B42BC?style=for-the-badge&logo=terraform&logoColor=white)

**A comprehensive guide to building CI/CD pipelines using Jenkins, Maven, SonarQube, and deploying to EKS**

[📖 Documentation](#-documentation) • [🛠️ Prerequisites](#️-prerequisites) • [🚀 Quick Start](#-quick-start) • [📚 Examples](#-examples)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Prerequisites](#️-prerequisites)
- [Quick Start](#-quick-start)
- [Documentation](#-documentation)
- [Project Structure](#-project-structure)
- [Pipeline Examples](#-pipeline-examples)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)

---

## 🎯 Overview

This repository contains everything you need to set up a complete CI/CD pipeline for Java applications:

- **Jenkins** - Continuous Integration/Continuous Deployment
- **Maven** - Build automation and dependency management
- **SonarQube** - Code quality and security analysis
- **Terraform** - Infrastructure as Code for EKS clusters
- **Kubernetes (EKS)** - Container orchestration and deployment

### What You'll Learn

✅ How to create Jenkins pipelines (Declarative and Scripted)  
✅ Maven build lifecycle and commands  
✅ SonarQube integration for code quality  
✅ Terraform for EKS cluster provisioning  
✅ Docker containerization and Kubernetes deployment  
✅ Three-tier application deployment strategies  

---

## 🛠️ Prerequisites

Before you begin, ensure you have the following installed:

### Required Software

| Tool | Version | Purpose |
|------|---------|---------|
| **Java JDK** | 11+ | Required for Maven and Java applications |
| **Maven** | 3.6+ | Build tool for Java projects |
| **Jenkins** | 2.400+ | CI/CD automation server |
| **Docker** | 20.10+ | Containerization platform |
| **kubectl** | Latest | Kubernetes command-line tool |
| **Terraform** | 1.0+ | Infrastructure as Code |
| **AWS CLI** | Latest | AWS service management |

### AWS Account Setup

- AWS Account with appropriate IAM permissions
- EC2 instance for Jenkins server
- Docker Hub account (for container registry)
- S3 bucket (for artifact storage)

### Jenkins Plugins

Install these essential plugins in Jenkins:

- ✅ **Pipeline** - Core pipeline functionality
- ✅ **Pipeline Stage View** - Visual pipeline progress
- ✅ **Git** - Source code management
- ✅ **SSH Build Agent** - Remote build agents
- ✅ **SonarQube Scanner** - Code quality analysis
- ✅ **Docker Pipeline** - Docker integration
- ✅ **Kubernetes** - K8s deployment

---

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/cdec-b45-jenkins.git
cd cdec-b45-jenkins
```

### 2. Set Up Jenkins Server

Follow the [Jenkins Setup Guide](jenkins.md#-installation) in `jenkins.md`

### 3. Install Maven

Follow the [Maven Installation Guide](maven.md#-installation) in `maven.md`

### 4. Configure SonarQube

Follow the [SonarQube Installation Guide](sonarqube-installation.md) for setup instructions

### 5. Create Your First Pipeline

Copy one of the example pipelines:

```bash
# For basic Maven build pipeline
cp jenkinsfile.jdp Jenkinsfile

# For EKS deployment pipeline
cp Jenkinsfile-eks-terraform.jdp Jenkinsfile
```

---

## 📚 Documentation

### Core Documentation

| Document | Description | Link |
|----------|-------------|------|
| **Jenkins Guide** | Complete guide to Jenkins pipelines, stages, and best practices | [📖 Read More](jenkins.md) |
| **Maven Guide** | Maven build lifecycle, commands, and POM configuration | [📖 Read More](maven.md) |
| **SonarQube Setup** | Step-by-step SonarQube installation and configuration | [📖 Read More](sonarqube-installation.md) |
| **Jenkins Server Setup** | Complete Jenkins server setup with Terraform, kubectl, AWS CLI, and Docker | [📖 Read More](jenkins-server-setup.md) |

### Pipeline Examples

| Pipeline | Description | File |
|----------|-------------|------|
| **Basic Maven Pipeline** | Pull, Build, Test, Deploy to Tomcat | `jenkinsfile.jdp` |
| **EKS Terraform Pipeline** | Infrastructure provisioning with Terraform | `Jenkinsfile-eks-terraform.jdp` |
| **Three-Tier Application** | Frontend + Backend deployment to EKS | `three-tier-using-pipeline.groovy` |
| **EKS Cluster Pipeline** | Complete EKS cluster setup | `eks-cluster-pipeline.jdp` |

---

## 📁 Project Structure

```
cdec-b45-jenkins/
│
├── 📄 README.md                          # This file
├── 📄 jenkins.md                         # Jenkins documentation
├── 📄 maven.md                           # Maven documentation
├── 📄 sonarqube-installation.md          # SonarQube setup guide
│
├── 🔧 Pipeline Files
│   ├── jenkinsfile.jdp                   # Basic Maven pipeline
│   ├── Jenkinsfile-eks-terraform.jdp     # EKS Terraform pipeline
│   ├── eks-cluster-pipeline.jdp          # EKS cluster pipeline
│   └── three-tier-using-pipeline.groovy  # Three-tier app pipeline
│
├── 🏗️ Infrastructure
│   ├── main.tf                          # Terraform main configuration
│   ├── var.tf                            # Terraform variables
│   └── output.tf                         # Terraform outputs
│
└── 📚 Question-Bank-DEVOPS/              # Practice questions
```

---

## 🔄 Pipeline Examples

### Example 1: Basic CI/CD Pipeline

```groovy
pipeline {
    agent any
    stages {
        stage('Pull') {
            steps {
                git 'https://github.com/your-repo/your-app.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                withSonarQubeEnv('sonar') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        stage('Deploy') {
            steps {
                deploy adapters: [tomcat8(...)], war: '**/*.war'
            }
        }
    }
}
```

### Example 2: Docker + Kubernetes Pipeline

See `three-tier-using-pipeline.groovy` for a complete example of:
- Building Docker images
- Pushing to Docker Hub
- Deploying to Kubernetes/EKS

---

## 🐛 Troubleshooting

### Common Issues

<details>
<summary><b>❌ Maven build fails</b></summary>

**Problem**: `mvn: command not found`

**Solution**:
```bash
# Install Maven
sudo apt install maven -y

# Or set Maven path in Jenkins
# Manage Jenkins → Configure System → Global Tool Configuration
```

</details>

<details>
<summary><b>❌ SonarQube connection fails</b></summary>

**Problem**: Cannot connect to SonarQube server

**Solution**:
1. Verify SonarQube is running: `systemctl status sonarqube`
2. Check Jenkins SonarQube configuration
3. Verify credentials in Jenkins credential store

</details>

<details>
<summary><b>❌ Docker permission denied</b></summary>

**Problem**: `permission denied while trying to connect to the Docker daemon socket`

**Solution**:
```bash
sudo gpasswd -a jenkins docker
sudo systemctl restart docker
sudo systemctl restart jenkins
```

</details>

<details>
<summary><b>❌ kubectl not found in Jenkins</b></summary>

**Problem**: Jenkins cannot execute kubectl commands

**Solution**:
```bash
# Install kubectl on Jenkins server
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x kubectl
sudo mv kubectl /usr/local/bin/
```

</details>

---

## 🎓 Learning Path

### For Beginners

1. 📖 Start with [Maven Basics](maven.md)
2. 🔧 Set up [Jenkins](jenkins.md#-installation)
3. 🏗️ Create your [first pipeline](jenkins.md#-creating-your-first-pipeline)
4. ✅ Integrate [SonarQube](sonarqube-installation.md)

### For Intermediate Users

1. 🐳 Learn [Docker integration](three-tier-using-pipeline.groovy)
2. ☸️ Deploy to [Kubernetes/EKS](eks-cluster-pipeline.jdp)
3. 🏗️ Use [Terraform for infrastructure](Jenkinsfile-eks-terraform.jdp)

### For Advanced Users

1. 🔄 Multi-stage pipelines with parallel execution
2. 🎯 Blue-Green deployments
3. 📊 Monitoring and observability
4. 🔐 Security best practices

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is for educational purposes.

---

## 🙏 Acknowledgments

- Jenkins Community
- Apache Maven Team
- SonarSource
- HashiCorp Terraform
- AWS EKS Team

---

<div align="center">

**Made with ❤️ for DevOps Learning**

⭐ Star this repo if you found it helpful!

</div>

