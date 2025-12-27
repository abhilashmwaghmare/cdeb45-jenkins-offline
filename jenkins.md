# types of pipeline 



scripted   pipeline  | declarative  pipeline

node                 |    pipeline

complex              |     easy , 




# plugins


pipeline
pipeline stage view 
git 
ssh-build agent 



scm -- git -- code pull 

maven -- build tool -- 


test -- sonarqube

deploy  --- container / k8s / tomcat 
  

  build now 



pipeline {
    agent any 
    stages {
        stage('Pull') { 
            steps {
                git 'https://github.com/shubhamkalsait/studentapp-ui.git'
            }
        }
        stage('Build') { 
            steps {
                sh '/opt/maven/bin/mvn clean package'
            }
        }
        stage('Test') { 
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar-creds') {
                    sh '/opt/maven/bin/mvn sonar:sonar'
                }
            }
        }
        stage('QualityGate') {
            steps {
                timeout(5) {
                waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Deploy') { 
            steps {
                deploy adapters: [tomcat8(credentialsId: 'tomcat-creds', path: '', url: 'http://54.175.163.58:8080')], contextPath: '/', onFailure: false, war: '**/*.war'
            }
        }
    }
}

