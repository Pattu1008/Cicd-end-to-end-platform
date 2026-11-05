pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven310'
    }

    stages {
        stage('Verify Tools') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
            }
        }
    }
}