
pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    tools {
        jdk 'JDK21'
        maven 'Maven310'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Product Service') {
            steps {
                dir('services/product-service') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Order Service') {
            steps {
                dir('services/order-service') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Test Product Service') {
            steps {
                dir('services/product-service') {
                    bat 'mvn test'
                }
            }
            post {
                always {
                    junit 'services/product-service/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Test Order Service') {
            steps {
                dir('services/order-service') {
                    bat 'mvn test'
                }
            }
            post {
                always {
                    junit 'services/order-service/target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        always {
            echo 'CI Pipeline execution completed.'
        }
    }
}
