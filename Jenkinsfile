pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo '----- Building Lib -----'
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
                        sh 'aws s3 cp ./target/likedancesport-media-management-lambda-1.0.jar s3://likedancesport-codebase/likedancesport-media-management-lambda-1.0.jar'
                    }
                    dir('video-upload-handler') {
                        sh 'aws s3 cp ./target/video-upload-handler-1.0.jar s3://likedancesport-codebase/video-upload-handler-1.0.jar'
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
                dir('compute-infra') {
                    sh 'ls -la'
                    sh 'cdk deploy'
                }
            }
        }
    }
}
