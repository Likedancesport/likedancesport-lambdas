pipeline {
    agent any

    environment {
        CODEARTIFACT_AUTH_TOKEN = sh(
                script: 'aws codeartifact get-authorization-token --domain likedancesport --domain-owner 066002146890 --region eu-central-1 --query authorizationToken --output text',
                returnStdout: true
        ).trim()
    }

    stages {
        stage('Deploy') {
            steps {
                echo 'Deploying....'
                sh 'ls -la'
                echo "${env.CODEARTIFACT_AUTH_TOKEN}"
                dir('software/create-video-lambda-spring-cloud') {
                    sh 'mvn dependency:resolve'
                }
                dir('compute-infra') {
                    sh 'ls -la'
                    sh 'cdk deploy'
                }
            }
        }
    }
}
