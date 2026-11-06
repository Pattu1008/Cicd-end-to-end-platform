pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    tools {
        jdk 'JDK21'
        maven 'Maven310'
    }


    environment {
    IMAGE_TAG = "${BUILD_NUMBER}"
    }   

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // Build Product Service
        stage('Build Product Service') {
            steps {
                dir('services/product-service') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        // Build Order Service
        stage('Build Order Service') {
            steps {
                dir('services/order-service') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        // Test Product Service and publish reports
        stage('Test Product Service') {
            steps {
                dir('services/product-service') {
                    bat 'mvn clean verify'
                }
            }
            post {
                always {
                    junit 'services/product-service/target/surefire-reports/*.xml'

                    recordCoverage(
                        id: 'product-service',
                        name: 'Product Service Coverage',
                        tools: [[
                            parser: 'JACOCO',
                            pattern: 'services/product-service/target/site/jacoco/jacoco.xml'
                        ]]
                    )
                }
            }
        }

        // SonarQube Analysis - Product Service
        stage('SonarQube Analysis - Product Service') {
            steps {
                withSonarQubeEnv('ProductSonarQube') {
                    dir('services/product-service') {
                        bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.3.0.6276:sonar -Dsonar.projectKey=product-service -Dsonar.projectName="Product Service" -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                    }
                }
            }
        }

        // Test Order Service and publish reports
        stage('Test Order Service') {
            steps {
                dir('services/order-service') {
                    bat 'mvn clean verify'
                }
            }
            post {
                always {
                    junit 'services/order-service/target/surefire-reports/*.xml'

                    recordCoverage(
                        id: 'order-service',
                        name: 'Order Service Coverage',
                        tools: [[
                            parser: 'JACOCO',
                            pattern: 'services/order-service/target/site/jacoco/jacoco.xml'
                        ]]
                    )
                }
            }
        }

        // SonarQube Analysis - Order Service
        stage('SonarQube Analysis - Order Service') {
            steps {
                withSonarQubeEnv('OrderSonarQube') {
                    dir('services/order-service') {
                        bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.3.0.6276:sonar -Dsonar.projectKey=order-service -Dsonar.projectName="Order Service" -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                    }
                }
            }
        }

        stage('Check Docker Access') {
            steps {
                  bat 'docker version'
                }
            }
    }

    post {
        always {
            echo 'CI Pipeline execution completed.'
        }
    }
}
