
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

        // 1. Checkout source code
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // 2. Build Product Service
        stage('Build Product Service') {
            steps {
                dir('services/product-service') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        // 3. Build Order Service
        stage('Build Order Service') {
            steps {
                dir('services/order-service') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        // 4. Test Product Service and publish reports
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

        // 5. SonarQube Analysis - Product Service
        stage('SonarQube Analysis - Product Service') {
            steps {
                withSonarQubeEnv('ProductSonarQube') {
                    dir('services/product-service') {
                        bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.3.0.6276:sonar -Dsonar.projectKey=product-service -Dsonar.projectName="Product Service" -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                    }
                }
            }
        }

        // 6. Test Order Service and publish reports
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

        // 7. SonarQube Analysis - Order Service
        stage('SonarQube Analysis - Order Service') {
            steps {
                withSonarQubeEnv('OrderSonarQube') {
                    dir('services/order-service') {
                        bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.3.0.6276:sonar -Dsonar.projectKey=order-service -Dsonar.projectName="Order Service" -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                    }
                }
            }
        }

        // 8. Check Docker access
        stage('Check Docker Access') {
            steps {
                bat 'docker version'
            }
        }
        // Build Product Docker Image
        stage('Build Product Docker Image') {
            steps {
                bat 'docker build -t product-service:%BUILD_NUMBER% ./services/product-service'
            }
        }

        // Build Order Docker Image
       stage('Build Order Docker Image') {
            steps {
               bat 'docker build -t order-service:%BUILD_NUMBER% ./services/order-service'
            }
        }

        // 9. Check Kubernetes configuration
        stage('Check Kubernetes Config') {
            steps {
                bat 'whoami'
                bat 'echo %KUBECONFIG%'
                bat 'where kubectl'
                bat 'kubectl config current-context'
                bat 'kubectl config view --minify'
            }
        }

        // 10. Check Kubernetes cluster access
        stage('Check Kubernetes Access') {
            steps {
                bat 'kubectl get nodes'
            }
        }
    }

    post {
        always {
            echo 'CI Pipeline execution completed.'
        }
    }
}
