pipeline {
     agent any 
        stages {
            stage ('stage-1-pull') {         
                 steps {
                     git branch: 'devops', url: 'https://github.com/abhilashmwaghmare/EASY_CRUD.git'

            }  
        }
                stage ('stage-2-docker-build-frontend') {         
                  steps {
                       sh '''cd frontend
                             docker build -t abhilash0104/easy-frontend:latest .'''

            }  
        }

              stage ('stage-3-docker-build-backend') {         
                  steps {
                    sh '''cd backend
                             docker build -t abhilash0104/easy-backend:latest .'''
            }  
        }

             stage ('stage-4-docker-push-backend-frontend') {         
                  steps {

                         sh '''docker push abhilash0104/easy-backend:latest
                               docker push abhilash0104/easy-frontend:latest '''
            }  
        }


      stage ('stage-5-deployment') {         
                  steps {
                       sh 'kubectl apply -f simple-deploy/'

            }  
        }

    }

} 