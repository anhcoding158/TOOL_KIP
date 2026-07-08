pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Environment') {
            steps {
                bat 'java -version'
                bat 'javac -version'
                bat 'jpackage --version'
            }
        }

        stage('Project Info') {
            steps {
                bat 'dir'
            }
        }
    }

    post {
        success {
            echo 'Build Success!'
        }

        failure {
            echo 'Build Failed!'
        }
    }
}