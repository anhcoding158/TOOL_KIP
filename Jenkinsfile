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

        stage('Package EXE') {
            steps {
                bat '''
                if exist package_input rmdir /S /Q package_input
                if exist release rmdir /S /Q release

                mkdir package_input

                copy TOOL_KIP.jar package_input

                jpackage ^
                    --input package_input ^
                    --dest release ^
                    --name DataProcessor ^
                    --main-jar TOOL_KIP.jar ^
                    --main-class AdvancedDataProcessorApp ^
                    --type exe ^
                    --win-dir-chooser ^
                    --win-menu ^
                    --win-shortcut
                '''
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'TOOL_KIP.jar,release/**', fingerprint: true
            }
        }
    }

    post {
        success {
            echo '=============================='
            echo ' BUILD SUCCESS'
            echo '=============================='
        }

        failure {
            echo '=============================='
            echo ' BUILD FAILED'
            echo '=============================='
        }
    }
}