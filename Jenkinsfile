pipeline {
    agent any

    environment {
        APP_NAME = "DataProcessor"
        APP_VERSION = "${env.BUILD_NUMBER}"
        MAIN_CLASS = "AdvancedDataProcessorApp"
        JAR_NAME = "TOOL_KIP.jar"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Environment') {
            steps {
                bat '''
                echo ==============================
                echo Java Environment
                echo ==============================

                java -version
                javac -version
                jpackage --version

                echo Build Number: %BUILD_NUMBER%
                '''
            }
        }

        stage('Compile') {
            steps {
                bat '''
                echo ==============================
                echo Compile Java
                echo ==============================

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
                echo ==============================
                echo Create JAR
                echo ==============================

                if exist %JAR_NAME% del %JAR_NAME%

                jar cfe %JAR_NAME% %MAIN_CLASS% -C out .
                '''
            }
        }

        stage('Create Installer') {
            steps {
                bat '''
                echo ==============================
                echo Create Setup EXE
                echo ==============================

                if exist package_input rmdir /S /Q package_input
                if exist release rmdir /S /Q release

                mkdir package_input

                copy %JAR_NAME% package_input

                jpackage ^
                    --type exe ^
                    --input package_input ^
                    --dest release ^
                    --name %APP_NAME% ^
                    --app-version %APP_VERSION% ^
                    --vendor "TOOL_KIP" ^
                    --main-jar %JAR_NAME% ^
                    --main-class %MAIN_CLASS% ^
                    --win-dir-chooser ^
                    --win-menu ^
                    --win-shortcut ^
                    --win-per-user-install

                REM Sau này có icon chỉ cần thêm:
                REM --icon icon.ico
                '''
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: '''
                    TOOL_KIP.jar,
                    release/**
                '''.trim(),
                fingerprint: true
            }
        }
    }

    post {

        success {
            echo "====================================="
            echo "BUILD SUCCESS"
            echo "Version : ${env.BUILD_NUMBER}"
            echo "Installer generated successfully."
            echo "====================================="
        }

        failure {
            echo "====================================="
            echo "BUILD FAILED"
            echo "Check Jenkins console log."
            echo "====================================="
        }

        always {
            cleanWs()
        }
    }
}