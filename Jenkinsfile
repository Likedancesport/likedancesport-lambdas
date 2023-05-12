pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
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
                    sh 'aws s3 cp ./target/likedancesport-layer-dependencies.jar s3://likedancesport-codebase/likedancesport-layer-dependencies'
                    dir('likedancesport-media-management-lambda') {
                        sh 'aws s3 cp ./target/likedancesport-media-management-lambda-1.0.jar s3://likedancesport-codebase/likedancesport-media-management-lambda.jar'
                    }
                    dir('learning-video-upload-handler') {
                        sh 'aws s3 cp ./target/learning-video-upload-handler-1.0.jar s3://likedancesport-codebase/mp4-video-upload-handler.jar'
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
                    sh 'cdk deploy --require-approval never --force'
                }
            }
        }
    }
}
