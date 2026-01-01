# 🔨 Apache Maven Build Tool Guide

<div align="center">

![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)

**Complete guide to Maven build lifecycle, commands, and best practices**

</div>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [What is Maven?](#-what-is-maven)
- [Installation](#-installation)
- [Project Structure](#-project-structure)
- [POM.xml Explained](#-pomxml-explained)
- [Maven Build Lifecycle](#-maven-build-lifecycle)
- [Common Maven Commands](#-common-maven-commands)
- [Maven Phases](#-maven-phases)
- [Dependency Management](#-dependency-management)
- [Best Practices](#-best-practices)
- [Troubleshooting](#-troubleshooting)

---

## 🎯 Introduction

Maven is a powerful build automation and project management tool primarily used for Java projects. It simplifies the build process by providing a standard way to build projects, manage dependencies, and generate documentation.

### Why Maven?

✅ **Standardized Build Process** - Consistent across all projects  
✅ **Dependency Management** - Automatic download and management  
✅ **Project Structure** - Convention over configuration  
✅ **Plugin Ecosystem** - Extensible with plugins  
✅ **Multi-module Support** - Manage complex projects easily  

---

## 🔍 What is Maven?

### Maven is a Build Tool

**Build Tool** = Software that automates the process of:
- Compiling source code
- Running tests
- Packaging applications
- Managing dependencies

### The Problem Maven Solves

**Before Maven:**
```
Raw Code Files:
├── src/
│   ├── Main.java
│   ├── User.java
│   ├── Product.java
│   └── ... (hundreds of files)
│
❌ Manual compilation
❌ Manual dependency management
❌ Inconsistent build process
❌ Time-consuming setup
```

**With Maven:**
```
✅ One command: mvn clean package
✅ Automatic compilation
✅ Automatic dependency download
✅ Standardized structure
✅ Consistent builds
```

### Maven vs Other Build Tools

| Build Tool | Language | Configuration | Learning Curve |
|------------|----------|---------------|----------------|
| **Maven** | Java | XML (POM.xml) | Moderate |
| **Gradle** | Java, Kotlin | Groovy/Kotlin DSL | Steep |
| **Ant** | Java | XML | Moderate |
| **MSBuild** | .NET | XML | Moderate |

> 💡 **Maven is the most popular** build tool for Java projects.

---

## 📥 Installation

### Prerequisites

Maven requires **Java JDK** to be installed first.

### Step 1: Install Java

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-11-jdk -y

# Verify installation
java -version
```

**Output should show:**
```
openjdk version "11.0.x"
OpenJDK Runtime Environment
OpenJDK 64-Bit Server VM
```

### Step 2: Install Maven

#### Method 1: Using Package Manager (Recommended)

```bash
# Ubuntu/Debian
sudo apt install maven -y

# Verify installation
mvn -version
```

#### Method 2: Manual Installation

```bash
# Download Maven
cd /tmp
wget https://dlcdn.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz

# Extract
tar -xzf apache-maven-3.9.5-bin.tar.gz

# Move to /opt
sudo mv apache-maven-3.9.5 /opt/maven

# Set environment variables
echo 'export M2_HOME=/opt/maven' >> ~/.bashrc
echo 'export PATH=$M2_HOME/bin:$PATH' >> ~/.bashrc

# Reload shell
source ~/.bashrc

# Verify
mvn -version
```

### Step 3: Verify Installation

```bash
mvn -version
```

**Expected Output:**
```
Apache Maven 3.9.5
Maven home: /opt/maven
Java version: 11.0.x
Java home: /usr/lib/jvm/java-11-openjdk-amd64
```

### Managing Multiple Java Versions

If you have multiple Java versions installed:

```bash
# List available Java versions
update-alternatives --list java

# Switch Java version
sudo update-alternatives --config java

# Select the version number and press Enter
```

---

## 📁 Project Structure

Maven follows a **convention over configuration** approach with a standard directory structure:

```
my-project/
│
├── pom.xml                    # Project Object Model (Configuration)
│
├── src/
│   ├── main/
│   │   ├── java/              # Java source files
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── App.java
│   │   │
│   │   ├── resources/         # Configuration files
│   │   │   ├── application.properties
│   │   │   └── log4j.xml
│   │   │
│   │   └── webapp/            # Web application files (for WAR)
│   │       └── WEB-INF/
│   │
│   └── test/
│       ├── java/              # Test source files
│       │   └── com/
│       │       └── example/
│       │           └── AppTest.java
│       │
│       └── resources/         # Test configuration files
│
└── target/                    # Build output (generated)
    ├── classes/               # Compiled classes
    ├── test-classes/          # Compiled test classes
    ├── my-app-1.0.jar         # JAR file
    └── surefire-reports/      # Test reports
```

### Directory Purpose

| Directory | Purpose |
|-----------|---------|
| `src/main/java` | Production Java source code |
| `src/main/resources` | Production resources (config files, etc.) |
| `src/test/java` | Test Java source code |
| `src/test/resources` | Test resources |
| `target/` | Build output (compiled classes, JARs, etc.) |

> ⚠️ **Note**: The `target/` directory is generated during build and should be in `.gitignore`

---

## 📄 POM.xml Explained

**POM** = **Project Object Model**

The `pom.xml` file is the heart of a Maven project. It contains all project configuration.

### Basic POM.xml Structure

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <!-- Project Information -->
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <!-- Project Metadata -->
    <name>My Application</name>
    <description>A sample Maven project</description>
    
    <!-- Properties -->
    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <!-- Dependencies -->
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <!-- Build Configuration -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Key Elements Explained

| Element | Description | Example |
|---------|-------------|---------|
| `groupId` | Organization/Company identifier | `com.example` |
| `artifactId` | Project name | `my-app` |
| `version` | Project version | `1.0.0` |
| `packaging` | Output type | `jar`, `war`, `pom` |
| `dependencies` | External libraries | JUnit, Spring, etc. |

### Packaging Types

| Type | Description | Output |
|------|-------------|--------|
| **jar** | Java Application Archive | `.jar` file |
| **war** | Web Application Archive | `.war` file |
| **pom** | Parent/Project Object Model | No output |
| **ear** | Enterprise Application Archive | `.ear` file |

---

## 🔄 Maven Build Lifecycle

Maven has three built-in lifecycles:

1. **default** - Build and deploy
2. **clean** - Clean project
3. **site** - Generate documentation

### Default Lifecycle Phases

```
validate → compile → test → package → verify → install → deploy
```

### Visual Representation

```
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│ validate│───▶│ compile │───▶│  test   │───▶│ package │───▶│ install │
└─────────┘    └─────────┘    └─────────┘    └─────────┘    └─────────┘
     │             │               │              │             │
     │             │               │              │             │
     ▼             ▼               ▼              ▼             ▼
  Check if     Compile        Run unit      Create JAR/    Install to
  project is   source code    tests         WAR file      local repo
  correct                     
```

---

## 🎯 Common Maven Commands

### Basic Commands

#### 1. Clean Project

```bash
mvn clean
```

**What it does:**
- Deletes the `target/` directory
- Removes all compiled files

**Use case:** Start fresh before building

---

#### 2. Compile Source Code

```bash
mvn compile
```

**What it does:**
- Compiles Java source files
- Outputs `.class` files to `target/classes/`

**Use case:** Check if code compiles without errors

---

#### 3. Run Tests

```bash
mvn test
```

**What it does:**
- Compiles source and test code
- Runs all unit tests
- Generates test reports in `target/surefire-reports/`

**Use case:** Verify code works correctly

---

#### 4. Package Application

```bash
mvn package
```

**What it does:**
- Compiles code
- Runs tests
- Creates JAR/WAR file in `target/` directory

**Use case:** Create deployable artifact

---

#### 5. Install to Local Repository

```bash
mvn install
```

**What it does:**
- Does everything `package` does
- Installs artifact to local Maven repository (`~/.m2/repository/`)

**Use case:** Make artifact available to other projects

---

#### 6. Clean and Package (Most Common)

```bash
mvn clean package
```

**What it does:**
- Cleans previous build
- Compiles, tests, and packages

**Use case:** Standard build command in CI/CD pipelines

---

#### 7. Skip Tests

```bash
mvn clean package -DskipTests
```

**What it does:**
- Builds without running tests
- ⚠️ **Warning**: Use only when necessary

**Use case:** Quick build when tests are slow

---

### Advanced Commands

#### Run Specific Test

```bash
mvn test -Dtest=AppTest
```

#### Skip Compilation (Only Package)

```bash
mvn package -Dmaven.main.skip=true
```

#### Show Dependency Tree

```bash
mvn dependency:tree
```

**Output:**
```
com.example:my-app:1.0.0
└── junit:junit:4.13.2
    └── org.hamcrest:hamcrest-core:1.3
```

#### Download Dependencies Only

```bash
mvn dependency:resolve
```

---

## 🔧 Maven Phases Explained

### Phase 1: Validate

```bash
mvn validate
```

- Validates project structure
- Checks if POM.xml is correct
- Verifies all required information is present

---

### Phase 2: Compile

```bash
mvn compile
```

- Compiles source code (`src/main/java`)
- Output: `target/classes/`

**Example:**
```java
// src/main/java/com/example/App.java
public class App {
    public static void main(String[] args) {
        System.out.println("Hello Maven!");
    }
}
```

After `mvn compile`:
```
target/classes/com/example/App.class
```

---

### Phase 3: Test

```bash
mvn test
```

- Compiles test code (`src/test/java`)
- Runs unit tests
- Generates reports

**Test Example:**
```java
// src/test/java/com/example/AppTest.java
import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void testApp() {
        assertTrue(true);
    }
}
```

---

### Phase 4: Package

```bash
mvn package
```

- Creates JAR or WAR file
- Location: `target/`

**JAR Example:**
```
target/my-app-1.0.0.jar
```

**WAR Example:**
```
target/my-app-1.0.0.war
```

---

### Phase 5: Verify

```bash
mvn verify
```

- Runs integration tests
- Performs quality checks
- Validates package integrity

---

### Phase 6: Install

```bash
mvn install
```

- Installs to local repository
- Location: `~/.m2/repository/com/example/my-app/1.0.0/`

---

### Phase 7: Deploy

```bash
mvn deploy
```

- Deploys to remote repository
- Requires repository configuration in POM.xml

---

## 📦 Dependency Management

### Adding Dependencies

Edit `pom.xml`:

```xml
<dependencies>
    <!-- JUnit for testing -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
    
    <!-- Spring Framework -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
        <version>5.3.21</version>
    </dependency>
</dependencies>
```

### Dependency Scopes

| Scope | Description | Example |
|-------|-------------|---------|
| **compile** | Default, available in all classpaths | Spring Core |
| **test** | Only for testing | JUnit |
| **provided** | Provided by runtime environment | Servlet API |
| **runtime** | Needed at runtime, not compile time | JDBC Driver |
| **system** | System path, not from repository | Custom JAR |

### Finding Dependencies

**Maven Central Repository**: [https://mvnrepository.com/](https://mvnrepository.com/)

**Search for dependency:**
1. Go to mvnrepository.com
2. Search for library name
3. Copy dependency XML
4. Paste into `pom.xml`

---

## ✅ Best Practices

### 1. Use Standard Directory Structure

✅ **Do**: Follow Maven conventions  
❌ **Don't**: Create custom directory structures

### 2. Version Your Dependencies

✅ **Do**: Specify exact versions  
❌ **Don't**: Use `LATEST` or `RELEASE`

### 3. Use Property Variables

✅ **Do**:
```xml
<properties>
    <junit.version>4.13.2</junit.version>
</properties>
<dependency>
    <version>${junit.version}</version>
</dependency>
```

### 4. Clean Before Building

✅ **Do**: `mvn clean package`  
❌ **Don't**: Just `mvn package` (may have stale files)

### 5. Don't Skip Tests in Production

✅ **Do**: Run all tests  
❌ **Don't**: Use `-DskipTests` in CI/CD

### 6. Use Maven Wrapper

✅ **Do**: Include `mvnw` in project  
❌ **Don't**: Require Maven installation

---

## 🐛 Troubleshooting

### Issue 1: Maven Command Not Found

**Symptoms**: `mvn: command not found`

**Solution**:
```bash
# Install Maven
sudo apt install maven -y

# Or add to PATH
export PATH=$PATH:/opt/maven/bin
```

---

### Issue 2: Java Version Mismatch

**Symptoms**: `Unsupported class file major version`

**Solution**:
```bash
# Check Java version
java -version

# Update POM.xml
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
</properties>
```

---

### Issue 3: Dependency Download Fails

**Symptoms**: `Could not resolve dependencies`

**Solution**:
```bash
# Clear local repository cache
rm -rf ~/.m2/repository

# Try again
mvn clean install
```

---

### Issue 4: Tests Fail

**Symptoms**: Build fails at test phase

**Solution**:
```bash
# Check test reports
cat target/surefire-reports/*.txt

# Run specific test to debug
mvn test -Dtest=AppTest
```

---

### Issue 5: Out of Memory

**Symptoms**: `java.lang.OutOfMemoryError`

**Solution**:
```bash
# Increase Maven memory
export MAVEN_OPTS="-Xmx1024m -Xms512m"
mvn clean package
```

---

## 📚 Quick Reference

### Command Cheat Sheet

| Command | Purpose |
|---------|---------|
| `mvn clean` | Delete target directory |
| `mvn compile` | Compile source code |
| `mvn test` | Run tests |
| `mvn package` | Create JAR/WAR |
| `mvn install` | Install to local repo |
| `mvn clean package` | Clean and build |
| `mvn clean package -DskipTests` | Build without tests |
| `mvn dependency:tree` | Show dependencies |
| `mvn help:effective-pom` | Show effective POM |

### Common File Locations

| Item | Location |
|------|----------|
| Source code | `src/main/java/` |
| Test code | `src/test/java/` |
| Compiled classes | `target/classes/` |
| JAR/WAR file | `target/*.jar` or `target/*.war` |
| Test reports | `target/surefire-reports/` |
| Local repository | `~/.m2/repository/` |

---

## 🎓 Learning Path

1. ✅ **Install Maven** - Get it running
2. ✅ **Create First Project** - `mvn archetype:generate`
3. ✅ **Understand POM.xml** - Learn configuration
4. ✅ **Run Build Commands** - Practice phases
5. ✅ **Add Dependencies** - Manage libraries
6. ✅ **Integrate with Jenkins** - CI/CD pipeline

---

<div align="center">

**Happy Building! 🔨**

[← Back to README](README.md) • [Next: SonarQube Setup →](sonarqube-installation.md)

</div>
