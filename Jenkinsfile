pipeline {
    agent any
    options {
        // This is required if you want to clean before build
        skipDefaultCheckout(true)
    }
    stages {
        stage('Build') {
            steps {
                // Clean before build
                cleanWs()
                // We need to explicitly checkout from SCM here
                checkout scm
                echo '----- Building Lib -----'
                dir('likedancesport-parent') {
                    sh 'mvn dependency:resolve'
                    sh 'mvn clean install'
                }
                dir('likedancesport-commons') {
                    sh 'mvn dependency:resolve'
                    sh 'mvn clean install'
                }
                echo '----- Building lambdas -----'
                dir('lambda-base-layer') {
                    sh 'mvn dependency:resolve'
                    sh 'mvn clean install'
                }

            }
        }

        stage('Copy') {
            steps {
                dir('lambda-base-layer') {
                    sh 'aws s3 cp ./target/likedancesport-layer-dependencies.jar s3://likedancesport-codebase/likedancesport-layer-dependencies.jar'
                    dir('likedancesport-media-management-lambda') {
                        sh 'aws s3 cp ./target/likedancesport-media-management-lambda-1.0.jar s3://likedancesport-codebase/likedancesport-media-management-lambda.jar'
                    }
                    dir('learning-video-upload-handler') {
                        sh 'aws s3 cp ./target/learning-video-upload-handler-1.0.jar s3://likedancesport-codebase/learning-video-upload-handler.jar'
                    }
                    dir('learning-video-transcoding-job-complete-handler') {
                        sh 'aws s3 cp ./target/learning-video-transcoding-job-complete-handler-1.0.jar s3://likedancesport-codebase/learning-video-transcoding-job-complete-handler.jar'

                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
                dir('compute-infra') {
                    sh 'ls -la'
                    sh "cdk deploy * --require-approval never --force"
                }
            }
        }
    }
    post {
        // Clean after build
        always {
            cleanWs(cleanWhenNotBuilt: false,
                    deleteDirs: true,
                    disableDeferredWipeout: true,
                    notFailBuild: true,
                    patterns: [[pattern: '.gitignore', type: 'INCLUDE'],
                               [pattern: '.propsfile', type: 'EXCLUDE']])
        }
    }
}
