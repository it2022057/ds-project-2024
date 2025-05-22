pipeline {

agent any

environment {
        DOCKER_TOKEN=credentials('github-token')
        DOCKER_USER='it2022057'
        DOCKER_SERVER='ghcr.io'
        DOCKER_PREFIX='ghcr.io/it2022057/ds-project-2024'
    }


stages {

    stage('Test') {
        steps {
            sh '''
                echo "Start testing"
                ./mvnw test
            '''
        }
    }

    stage('Docker build and push') {
            steps {
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    docker build --rm -t $DOCKER_PREFIX:$TAG -t $DOCKER_PREFIX:latest -f Dockerfile .
                '''

                sh '''
                    echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                    docker push $DOCKER_PREFIX --all-tags
                '''
            }
        }

    }

//    post {
//        always {
//            mail  to: "tsadimas@gmail.com", from: "tsadimas@gmail.com", body: "Project ${env.JOB_NAME} <br>, Build status ${currentBuild.currentResult} <br> Build Number: ${env.BUILD_NUMBER} <br> Build URL: ${env.BUILD_URL}", subject: "JENKINS: Project name -> ${env.JOB_NAME}, Build -> ${currentBuild.currentResult}"
//        }
//    }


}