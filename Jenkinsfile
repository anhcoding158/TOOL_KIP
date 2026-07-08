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

        stage('Compile') {
            steps {
                bat '''
                if exist out rmdir /S /Q out
                mkdir out

                javac -encoding UTF-8 ^
                    -d out ^
                    src\\AdvancedDataProcessorApp.java ^
                    src\\Main.java
                '''
            }
        }

        stage('Create JAR') {
            steps {
                bat '''
                if exist TOOL_KIP.jar del TOOL_KIP.jar

                jar cfe TOOL_KIP.jar AdvancedDataProcessorApp -C out .
                '''
            }
        }

        stage('Archive JAR') {
            steps {
                archiveArtifacts artifacts: 'TOOL_KIP.jar', fingerprint: true
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