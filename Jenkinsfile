pipeline {
    agent any

    stages {
        stage('Deploy') {
            steps {
                echo 'Deploying....'
                sh 'cd compute-infra'
                cd 'cdk deploy'
            }
        }
    }
}
