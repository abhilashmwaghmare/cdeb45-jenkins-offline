# 🚀 Jenkins CI/CD Pipeline Guide

<div align="center">

![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white)

**Complete guide to Jenkins pipelines, stages, and best practices**

</div>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [Types of Pipelines](#-types-of-pipelines)
- [Installation](#-installation)
- [Essential Plugins](#-essential-plugins)
- [Pipeline Stages Explained](#-pipeline-stages-explained)
- [Creating Your First Pipeline](#-creating-your-first-pipeline)
- [Advanced Concepts](#-advanced-concepts)
- [Best Practices](#-best-practices)
- [Troubleshooting](#-troubleshooting)

---

## 🎯 Introduction

Jenkins is an open-source automation server that enables developers to build, test, and deploy applications continuously. It's the backbone of modern CI/CD practices.

### What is CI/CD?

- **CI (Continuous Integration)**: Automatically build and test code changes
- **CD (Continuous Deployment)**: Automatically deploy code to production

### Why Jenkins?

✅ **Open Source** - Free and community-driven  
✅ **Extensible** - Thousands of plugins available  
✅ **Flexible** - Supports multiple languages and platforms  
✅ **Scalable** - Can handle projects of any size  

---

## 🔄 Types of Pipelines

Jenkins supports two types of pipelines:

### 1. Scripted Pipeline

**Characteristics:**
- Uses Groovy-based DSL
- More flexible and powerful
- Complex syntax
- Better for advanced use cases

**Example:**
```groovy
node {
    stage('Build') {
        sh 'mvn clean package'
    }
    stage('Test') {
        sh 'mvn test'
    }
}
```

### 2. Declarative Pipeline ⭐ (Recommended)

**Characteristics:**
- Simpler, more readable syntax
- Easier to learn
- Better error handling
- Preferred for most use cases

**Example:**
```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}
```

### Comparison Table

| Feature | Scripted Pipeline | Declarative Pipeline |
|---------|-------------------|----------------------|
| **Syntax** | Groovy DSL | Simplified YAML-like |
| **Complexity** | High | Low |
| **Learning Curve** | Steep | Gentle |
| **Flexibility** | Very High | Moderate |
| **Error Handling** | Manual | Built-in |
| **Best For** | Advanced users | Beginners & Teams |

> 💡 **Recommendation**: Start with Declarative Pipeline for easier learning and maintenance.

---

## 📥 Installation

### Step 1: Install Java

Jenkins requires Java to run:

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-11-jdk -y

# Verify installation
java -version
```

### Step 2: Install Jenkins

```bash
# Add Jenkins repository
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null

echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null

# Install Jenkins
sudo apt-get update
sudo apt-get install jenkins -y

# Start Jenkins
sudo systemctl start jenkins
sudo systemctl enable jenkins

# Check status
sudo systemctl status jenkins
```

### Step 3: Initial Setup

1. **Access Jenkins**: Open `http://your-server-ip:8080`
2. **Get Initial Password**:
   ```bash
   sudo cat /var/lib/jenkins/secrets/initialAdminPassword
   ```
3. **Install Suggested Plugins**
4. **Create Admin User**
5. **Configure Jenkins URL**

---

## 🔌 Essential Plugins

Install these plugins for a complete CI/CD setup:

### Core Plugins

| Plugin | Purpose | Required |
|--------|---------|----------|
| **Pipeline** | Core pipeline functionality | ✅ Yes |
| **Pipeline Stage View** | Visual pipeline progress | ✅ Yes |
| **Git** | Source code management | ✅ Yes |
| **SSH Build Agent** | Remote build agents | ⚠️ Optional |
| **Blue Ocean** | Modern UI for pipelines | ⚠️ Optional |

### Integration Plugins

| Plugin | Purpose |
|--------|---------|
| **SonarQube Scanner** | Code quality analysis |
| **Docker Pipeline** | Docker integration |
| **Kubernetes** | K8s deployment |
| **AWS Steps** | AWS service integration |
| **Ansible** | Configuration management |

### How to Install Plugins

1. Go to **Manage Jenkins** → **Manage Plugins**
2. Click **Available** tab
3. Search for the plugin name
4. Check the box and click **Install without restart**
5. Wait for installation to complete

---

## 🔄 Pipeline Stages Explained

A typical CI/CD pipeline consists of these stages:

```
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│   SCM   │───▶│  Build  │───▶│  Test   │───▶│ Quality │───▶│ Deploy  │
│  (Pull) │    │         │    │         │    │  Gate   │    │         │
└─────────┘    └─────────┘    └─────────┘    └─────────┘    └─────────┘
```

### Stage 1: SCM (Source Code Management) - Pull

**Purpose**: Pull the latest code from version control

**Tools**: Git, SVN, Mercurial

**Example:**
```groovy
stage('Pull') {
    steps {
        git branch: 'main', 
            url: 'https://github.com/your-username/your-repo.git'
    }
}
```

### Stage 2: Build

**Purpose**: Compile source code and create artifacts

**Tools**: Maven, Gradle, npm, Docker

**Example:**
```groovy
stage('Build') {
    steps {
        sh 'mvn clean package'
        // Creates .jar or .war file in target/ directory
    }
}
```

### Stage 3: Test

**Purpose**: Run unit tests and code quality checks

**Tools**: JUnit, SonarQube, Selenium

**Example:**
```groovy
stage('Test') {
    steps {
        // Run unit tests
        sh 'mvn test'
        
        // Code quality analysis
        withSonarQubeEnv(installationName: 'sonar', 
                        credentialsId: 'sonar-creds') {
            sh 'mvn sonar:sonar'
        }
    }
}
```

### Stage 4: Quality Gate

**Purpose**: Ensure code meets quality standards before deployment

**Example:**
```groovy
stage('QualityGate') {
    steps {
        timeout(time: 5, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
    }
}
```

### Stage 5: Deploy

**Purpose**: Deploy application to target environment

**Targets**: 
- 🐳 **Containers** (Docker)
- ☸️ **Kubernetes/EKS**
- 🖥️ **Application Servers** (Tomcat, JBoss)
- ☁️ **Cloud Platforms** (AWS, Azure, GCP)

**Example - Tomcat Deployment:**
```groovy
stage('Deploy') {
    steps {
        deploy adapters: [
            tomcat8(
                credentialsId: 'tomcat-creds',
                path: '',
                url: 'http://your-server:8080'
            )
        ], 
        contextPath: '/',
        onFailure: false,
        war: '**/*.war'
    }
}
```

**Example - Kubernetes Deployment:**
```groovy
stage('Deploy') {
    steps {
        sh 'kubectl apply -f k8s/deployment.yaml'
        sh 'kubectl rollout status deployment/my-app'
    }
}
```

---

## 🎓 Creating Your First Pipeline

### Method 1: Jenkinsfile in Repository (Recommended)

1. **Create a Jenkinsfile** in your project root:

```groovy
pipeline {
    agent any
    
    stages {
        stage('Pull') {
            steps {
                git branch: 'main', 
                    url: 'https://github.com/<username>/studentapp-ui.git'
            }
        }
        
        stage('Build') {
            steps {
                sh '/opt/maven/bin/mvn clean package'
            }
        }
        
        stage('Test') {
            steps {
                withSonarQubeEnv(
                    installationName: 'sonar', 
                    credentialsId: 'sonar-creds'
                ) {
                    sh '/opt/maven/bin/mvn sonar:sonar'
                }
            }
        }
        
        stage('QualityGate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        stage('Deploy') {
            steps {
                deploy adapters: [
                    tomcat8(
                        credentialsId: 'tomcat-creds',
                        path: '',
                        url: 'http://54.175.163.58:8080'
                    )
                ], 
                contextPath: '/',
                onFailure: false,
                war: '**/*.war'
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline succeeded! 🎉'
        }
        failure {
            echo 'Pipeline failed! ❌'
        }
        always {
            cleanWs() // Clean workspace
        }
    }
}
```

2. **Create Pipeline Job in Jenkins**:
   - Click **New Item**
   - Enter name: `my-first-pipeline`
   - Select **Pipeline**
   - Click **OK**
   - In **Pipeline** section:
     - Definition: **Pipeline script from SCM**
     - SCM: **Git**
     - Repository URL: Your repository URL
     - Branch: `*/main`
     - Script Path: `Jenkinsfile`
   - Click **Save**

3. **Run Pipeline**:
   - Click **Build Now**
   - Watch the pipeline progress in **Stage View**

### Method 2: Pipeline Script in Jenkins UI

1. Create a new Pipeline job
2. In Pipeline section, select **Pipeline script**
3. Paste your pipeline code directly
4. Click **Save** and **Build Now**

---

## 🚀 Advanced Concepts

### Environment Variables

```groovy
pipeline {
    agent any
    
    environment {
        MAVEN_HOME = '/opt/maven'
        APP_VERSION = '1.0.0'
        DOCKER_IMAGE = 'myapp:latest'
    }
    
    stages {
        stage('Build') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean package"
            }
        }
    }
}
```

### Parallel Execution

```groovy
stage('Test') {
    parallel {
        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Integration Tests') {
            steps {
                sh 'mvn verify'
            }
        }
    }
}
```

### Conditional Stages

```groovy
stage('Deploy to Production') {
    when {
        branch 'main'
        expression { env.BUILD_NUMBER.toInteger() % 10 == 0 }
    }
    steps {
        sh 'deploy-to-prod.sh'
    }
}
```

### Post Actions

```groovy
post {
    always {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        junit 'target/surefire-reports/*.xml'
    }
    success {
        emailext (
            subject: "✅ Build Success: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
            body: "Build succeeded!",
            to: "team@example.com"
        )
    }
    failure {
        emailext (
            subject: "❌ Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
            body: "Build failed. Please check the logs.",
            to: "team@example.com"
        )
    }
}
```

### Credentials Management

**Store credentials securely:**

1. Go to **Manage Jenkins** → **Manage Credentials**
2. Add credentials (Username/Password, SSH, Token, etc.)
3. Use in pipeline:

```groovy
stage('Deploy') {
    steps {
        withCredentials([
            usernamePassword(
                credentialsId: 'docker-creds',
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_PASS'
            )
        ]) {
            sh '''
                echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                docker push myapp:latest
            '''
        }
    }
}
```

---

## ✅ Best Practices

### 1. Use Jenkinsfile in Repository

✅ **Do**: Store Jenkinsfile in your Git repository  
❌ **Don't**: Hardcode pipeline in Jenkins UI

### 2. Use Declarative Pipeline

✅ **Do**: Use declarative syntax for readability  
❌ **Don't**: Use scripted unless necessary

### 3. Fail Fast

✅ **Do**: Use `abortPipeline: true` in quality gates  
❌ **Don't**: Continue deployment with failing tests

### 4. Clean Workspace

✅ **Do**: Clean workspace in `post { always }` block  
❌ **Don't**: Leave artifacts from previous builds

### 5. Use Credentials Plugin

✅ **Do**: Store secrets in Jenkins credentials  
❌ **Don't**: Hardcode passwords in pipeline

### 6. Version Your Artifacts

✅ **Do**: Tag artifacts with version numbers  
❌ **Don't**: Use `latest` tag in production

### 7. Use Pipeline Libraries

✅ **Do**: Create shared libraries for common functions  
❌ **Don't**: Duplicate code across pipelines

### 8. Add Notifications

✅ **Do**: Notify team on build status  
❌ **Don't**: Leave team unaware of failures

---

## 🐛 Troubleshooting

### Issue 1: Pipeline Fails at Pull Stage

**Symptoms**: `git: command not found`

**Solution**:
```bash
# Install Git on Jenkins server
sudo apt install git -y

# Verify installation
git --version
```

### Issue 2: Maven Command Not Found

**Symptoms**: `mvn: command not found`

**Solution**:
```bash
# Install Maven
sudo apt install maven -y

# Or configure Maven in Jenkins:
# Manage Jenkins → Global Tool Configuration → Maven
```

### Issue 3: Permission Denied Errors

**Symptoms**: `Permission denied` when executing scripts

**Solution**:
```bash
# Make scripts executable
chmod +x your-script.sh

# Or run with explicit shell
sh 'bash your-script.sh'
```

### Issue 4: SonarQube Connection Failed

**Symptoms**: Cannot connect to SonarQube server

**Solution**:
1. Verify SonarQube is running: `systemctl status sonarqube`
2. Check SonarQube URL in Jenkins configuration
3. Verify credentials are correct
4. Check network connectivity

### Issue 5: Docker Permission Denied

**Symptoms**: `permission denied while trying to connect to the Docker daemon socket`

**Solution**:
```bash
# Add Jenkins user to docker group
sudo gpasswd -a jenkins docker

# Restart services
sudo systemctl restart docker
sudo systemctl restart jenkins
```

---

## 📚 Additional Resources

- [Jenkins Official Documentation](https://www.jenkins.io/doc/)
- [Pipeline Syntax Reference](https://www.jenkins.io/doc/book/pipeline/syntax/)
- [Jenkins Pipeline Examples](https://github.com/jenkinsci/pipeline-examples)
- [Blue Ocean Documentation](https://www.jenkins.io/doc/book/blueocean/)

---

## 🎯 Quick Reference

### Common Pipeline Snippets

**Basic Pipeline Structure:**
```groovy
pipeline {
    agent any
    stages {
        stage('Stage Name') {
            steps {
            // Commands here
        }
    }
}
```

**Git Checkout:**
```groovy
git branch: 'main', url: 'https://github.com/user/repo.git'
```

**Maven Build:**
```groovy
sh 'mvn clean package'
```

**Docker Build & Push:**
```groovy
sh 'docker build -t image:tag .'
sh 'docker push image:tag'
```

**Kubernetes Deploy:**
```groovy
sh 'kubectl apply -f deployment.yaml'
```

---

<div align="center">

**Happy Pipelining! 🚀**

[← Back to README](README.md)

</div>
