# 🔍 SonarQube Installation & Configuration Guide

<div align="center">

![SonarQube](https://img.shields.io/badge/SonarQube-4E9BCD?style=for-the-badge&logo=SonarQube&logoColor=white)

**Complete step-by-step guide to install and configure SonarQube for code quality analysis**

</div>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [What is SonarQube?](#-what-is-sonarqube)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Installation Steps](#-installation-steps)
- [Configuration](#-configuration)
- [Jenkins Integration](#-jenkins-integration)
- [Best Practices](#-best-practices)
- [Troubleshooting](#-troubleshooting)

---

## 🎯 Introduction

SonarQube is an open-source platform for continuous inspection of code quality. It performs automatic reviews with static analysis of code to detect bugs, code smells, and security vulnerabilities.

### Why SonarQube?

✅ **Code Quality** - Detects bugs and code smells  
✅ **Security** - Identifies security vulnerabilities  
✅ **Coverage** - Measures test coverage  
✅ **Maintainability** - Technical debt tracking  
✅ **Multi-language** - Supports 25+ programming languages  

---

## 🔍 What is SonarQube?

### Quality Gate

A **Quality Gate** is a set of conditions that your code must meet before it can be considered production-ready.

**Example Quality Gate:**
- ✅ No new bugs
- ✅ Code coverage > 80%
- ✅ No security vulnerabilities
- ✅ Maintainability rating A

### Advantages of SonarQube

| Feature | Benefit |
|---------|---------|
| **Automated Code Review** | Catch issues before code review |
| **Technical Debt Tracking** | Measure and reduce technical debt |
| **Security Analysis** | Find security vulnerabilities early |
| **Code Coverage** | Ensure adequate test coverage |
| **Multi-language Support** | Works with Java, Python, JavaScript, etc. |

---

## 🏗️ Architecture

### SonarQube Components

```
┌─────────────────────────────────────────────────────────┐
│                    SonarQube Server                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │   Web UI     │  │   Compute    │  │ Elasticsearch│  │
│  │  (Port 9000) │  │   Engine     │  │   Database   │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                        ▲
                        │
                        │ Analysis Results
                        │
┌─────────────────────────────────────────────────────────┐
│              SonarQube Scanner (Client)                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │   Jenkins    │  │   Maven      │  │   CLI        │  │
│  │   Plugin     │  │   Plugin     │  │   Scanner    │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### Component Types

1. **Web UI** - User interface for viewing analysis results
2. **Compute Engine** - Processes analysis reports
3. **Elasticsearch** - Stores analysis data
4. **Database** - Stores configuration and metadata

---

## 📋 Prerequisites

### System Requirements

| Component | Minimum | Recommended |
|-----------|---------|-------------|
| **RAM** | 2 GB | 4 GB+ |
| **CPU** | 2 cores | 4+ cores |
| **Disk** | 10 GB | 50 GB+ |
| **OS** | Linux, Windows, macOS | Linux |

### Software Requirements

- ✅ **Java JDK 17** (Required)
- ✅ **PostgreSQL 12+** (Database)
- ✅ **Elasticsearch** (Included in SonarQube)

---

## 🚀 Installation Steps

### Step 1: Install Java JDK 17

```bash
# Update package list
sudo apt update -y

# Install OpenJDK 17
sudo apt install openjdk-17-jdk -y

# Verify installation
java -version
```

**Expected Output:**
```
openjdk version "17.0.x"
OpenJDK Runtime Environment
OpenJDK 64-Bit Server VM
```

---

### Step 2: Install and Configure PostgreSQL Database

#### Install PostgreSQL

```bash
# Install PostgreSQL
sudo apt install postgresql -y

# Start PostgreSQL service
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Check status
sudo systemctl status postgresql
```

#### Create SonarQube Database and User

```bash
# Switch to postgres user
sudo -u postgres psql
```

**Inside PostgreSQL prompt, run:**

```sql
-- Create user for SonarQube
CREATE USER linux WITH PASSWORD 'redhat';

-- Create database
CREATE DATABASE sonarqube;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE sonarqube TO linux;

-- Connect to sonarqube database
\c sonarqube;

-- Grant schema privileges
GRANT ALL PRIVILEGES ON SCHEMA public TO linux;

-- Exit PostgreSQL
\q
```

**Verify Database:**

```bash
# Test connection
sudo -u postgres psql -U linux -d sonarqube -h localhost
# Enter password: redhat
# Type \q to exit
```

---

### Step 3: Configure Linux System Settings

SonarQube requires specific system configurations:

```bash
# Configure virtual memory
sudo sysctl -w vm.max_map_count=524288

# Configure file descriptors
sudo sysctl -w fs.file-max=131072

# Set user limits
ulimit -n 131072
ulimit -u 8192

# Make changes permanent
echo "vm.max_map_count=524288" | sudo tee -a /etc/sysctl.conf
echo "fs.file-max=131072" | sudo tee -a /etc/sysctl.conf
```

**Add to `/etc/security/limits.conf`:**

```bash
sudo vim /etc/security/limits.conf
```

**Add these lines:**
```
sonar   -   nofile   131072
sonar   -   nproc    8192
```

---

### Step 4: Download and Install SonarQube

```bash
# Navigate to /opt directory
cd /opt

# Download SonarQube (Community Edition)
sudo wget https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-25.5.0.107428.zip

# Install unzip if not already installed
sudo apt install unzip -y

# Extract SonarQube
sudo unzip sonarqube-25.5.0.107428.zip

# Rename directory for easier access
sudo mv sonarqube-25.5.0.107428 sonar

# Set ownership
sudo chown -R sonar:sonar /opt/sonar
```

---

### Step 5: Configure SonarQube

#### Edit SonarQube Configuration

```bash
# Navigate to SonarQube directory
cd /opt/sonar

# Edit configuration file
sudo vim conf/sonar.properties
```

**Add/Modify these lines:**

```properties
# Database Configuration
sonar.jdbc.username=linux
sonar.jdbc.password=redhat
sonar.jdbc.url=jdbc:postgresql://localhost/sonarqube

# Web Server Configuration
sonar.web.host=0.0.0.0
sonar.web.port=9000

# Optional: Set Java options
sonar.web.javaOpts=-Xmx512m -Xms128m
```

**Save and exit** (Press `Esc`, then `:wq` in vim)

---

### Step 6: Create SonarQube User

```bash
# Create dedicated user for SonarQube
sudo useradd sonar -m

# Set password (optional)
sudo passwd sonar

# Add to necessary groups
sudo usermod -aG sudo sonar
```

---

### Step 7: Set Permissions

```bash
# Change ownership of SonarQube directory
sudo chown -R sonar:sonar /opt/sonar

# Verify permissions
ls -la /opt/sonar
```

---

### Step 8: Start SonarQube

```bash
# Switch to sonar user
sudo su - sonar

# Navigate to SonarQube bin directory
cd /opt/sonar/bin/linux-x86-64

# Start SonarQube
./sonar.sh start

# Check status
./sonar.sh status
```

**Expected Output:**
```
SonarQube is running (12345).
```

---

### Step 9: Access SonarQube Web UI

1. **Open browser** and navigate to:
   ```
   http://your-server-ip:9000
   ```

2. **Default Credentials:**
   - Username: `admin`
   - Password: `admin`

3. **First Login:**
   - You'll be prompted to change the password
   - Create a strong password and save it

---

## ⚙️ Configuration

### Generate Authentication Token

1. Log in to SonarQube
2. Go to **My Account** → **Security**
3. Generate a new token
4. **Save the token** (you'll need it for Jenkins)

**Example Token:**
```
squ_1234567890abcdef1234567890abcdef12345678
```

### Configure Quality Gates

1. Go to **Quality Gates**
2. Click **Create** to add a new quality gate
3. Configure conditions:
   - Coverage on New Code > 80%
   - Duplicated Lines on New Code < 3%
   - Maintainability Rating on New Code = A
   - Reliability Rating on New Code = A
   - Security Rating on New Code = A

---

## 🔗 Jenkins Integration

### Step 1: Install SonarQube Plugin in Jenkins

1. Go to **Manage Jenkins** → **Manage Plugins**
2. Search for **SonarQube Scanner**
3. Install the plugin
4. Restart Jenkins if required

### Step 2: Configure SonarQube Server in Jenkins

1. Go to **Manage Jenkins** → **Configure System**
2. Scroll to **SonarQube servers**
3. Click **Add SonarQube**
4. Configure:
   - **Name**: `sonar` (or any name)
   - **Server URL**: `http://your-sonarqube-server:9000`
   - **Server authentication token**: Paste the token from SonarQube
5. Click **Save**

### Step 3: Configure SonarQube Scanner

1. Go to **Manage Jenkins** → **Global Tool Configuration**
2. Scroll to **SonarQube Scanner**
3. Click **Add SonarQube Scanner**
4. Configure:
   - **Name**: `sonar-scanner`
   - **Install automatically**: Check this
   - **Version**: Latest
5. Click **Save**

### Step 4: Use in Pipeline

**Example Jenkinsfile:**

```groovy
pipeline {
    agent any
    
    stages {
        stage('Pull') {
            steps {
                git branch: 'main', 
                    url: 'https://github.com/your-repo/your-app.git'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        
        stage('Test') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', 
                                credentialsId: 'sonar-creds') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=myproject'
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        stage('Deploy') {
            steps {
                sh 'mvn deploy'
            }
        }
    }
}
```

---

## ✅ Best Practices

### 1. Regular Updates

✅ **Do**: Keep SonarQube updated  
❌ **Don't**: Use outdated versions

### 2. Strong Passwords

✅ **Do**: Use strong passwords and tokens  
❌ **Don't**: Use default credentials in production

### 3. Quality Gates

✅ **Do**: Enforce quality gates in CI/CD  
❌ **Don't**: Deploy code that fails quality checks

### 4. Regular Analysis

✅ **Do**: Run analysis on every commit  
❌ **Don't**: Analyze only before releases

### 5. Fix Critical Issues

✅ **Do**: Fix critical bugs and vulnerabilities immediately  
❌ **Don't**: Ignore security issues

---

## 🐛 Troubleshooting

### Issue 1: SonarQube Won't Start

**Symptoms**: `./sonar.sh start` fails

**Solution:**
```bash
# Check logs
tail -f /opt/sonar/logs/sonar.log

# Common issues:
# 1. Java not found - Install JDK 17
# 2. Database connection failed - Check PostgreSQL
# 3. Port 9000 already in use - Change port or stop conflicting service
```

---

### Issue 2: Database Connection Failed

**Symptoms**: `Could not connect to database`

**Solution:**
```bash
# Verify PostgreSQL is running
sudo systemctl status postgresql

# Test database connection
sudo -u postgres psql -U linux -d sonarqube -h localhost

# Check sonar.properties configuration
cat /opt/sonar/conf/sonar.properties | grep jdbc
```

---

### Issue 3: Out of Memory Error

**Symptoms**: `java.lang.OutOfMemoryError`

**Solution:**
```bash
# Edit sonar.properties
sudo vim /opt/sonar/conf/sonar.properties

# Increase memory
sonar.web.javaOpts=-Xmx1024m -Xms512m

# Restart SonarQube
./sonar.sh restart
```

---

### Issue 4: Cannot Access Web UI

**Symptoms**: Cannot connect to `http://server:9000`

**Solution:**
```bash
# Check if SonarQube is running
./sonar.sh status

# Check firewall
sudo ufw allow 9000/tcp

# Check if port is listening
sudo netstat -tlnp | grep 9000
```

---

### Issue 5: Jenkins Integration Fails

**Symptoms**: Jenkins cannot connect to SonarQube

**Solution:**
1. Verify SonarQube URL in Jenkins configuration
2. Check authentication token is correct
3. Verify network connectivity between Jenkins and SonarQube
4. Check SonarQube logs for errors

---

## 📊 Understanding SonarQube Metrics

### Code Smells

Issues that make code harder to maintain:
- Long methods
- Duplicated code
- Complex code

### Bugs

Actual errors in code that will cause failures:
- Null pointer exceptions
- Logic errors
- Resource leaks

### Vulnerabilities

Security issues:
- SQL injection
- Cross-site scripting (XSS)
- Insecure dependencies

### Coverage

Percentage of code covered by tests:
- **80%+** = Good
- **60-80%** = Acceptable
- **<60%** = Needs improvement

---

## 🎓 Quick Reference

### SonarQube Commands

| Command | Purpose |
|---------|---------|
| `./sonar.sh start` | Start SonarQube |
| `./sonar.sh stop` | Stop SonarQube |
| `./sonar.sh restart` | Restart SonarQube |
| `./sonar.sh status` | Check status |
| `./sonar.sh console` | View logs in console |

### Important Files

| File | Location | Purpose |
|------|----------|---------|
| Configuration | `/opt/sonar/conf/sonar.properties` | SonarQube settings |
| Logs | `/opt/sonar/logs/` | Application logs |
| Data | `/opt/sonar/data/` | Analysis data |
| Extensions | `/opt/sonar/extensions/` | Plugins |

### Default Ports

| Service | Port |
|---------|------|
| SonarQube Web UI | 9000 |
| PostgreSQL | 5432 |

---

## 🔐 Security Checklist

- [ ] Changed default admin password
- [ ] Generated authentication tokens
- [ ] Configured firewall rules
- [ ] Enabled HTTPS (for production)
- [ ] Regular backups of database
- [ ] Updated SonarQube regularly
- [ ] Restricted network access

---

<div align="center">

**Happy Code Quality Analysis! 🔍**

[← Back to README](README.md) • [Jenkins Guide →](jenkins.md)

</div>

