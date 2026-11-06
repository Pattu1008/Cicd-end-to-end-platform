
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

        // 9. Build Product Docker Image
        stage('Build Product Docker Image') {
            steps {
                bat 'docker build -t product-service:%BUILD_NUMBER% ./services/product-service'
            }
        }

        // 10. Build Order Docker Image
        stage('Build Order Docker Image') {
            steps {
                bat 'docker build -t order-service:%BUILD_NUMBER% ./services/order-service'
            }
        }

        // 11. Load Product Image into Kind
        stage('Load Product Image into Kind') {
            steps {
                bat '"C:\\Tools\\kind\\kind.exe" load docker-image product-service:%BUILD_NUMBER% --name devops-lab'
            }
        }

        // 12. Load Order Image into Kind
        stage('Load Order Image into Kind') {
            steps {
                bat '"C:\\Tools\\kind\\kind.exe" load docker-image order-service:%BUILD_NUMBER% --name devops-lab'
            }
        }

        // 13. Check Kubernetes access
        stage('Check Kubernetes Access') {
            steps {
                bat 'kubectl config current-context'
                bat 'kubectl get nodes'
            }
        }

        // 14. Check Helm access
        stage('Check Helm Access') {
            steps {
                bat '"C:\\Users\\Prateek Sangle\\bin\\helm.exe" version'
            }
        }

        /*
        // OLD DEPLOYMENT METHOD - Product Service using Kubernetes YAML

        stage('Deploy Product Service') {
            steps {
                powershell '''
                    $yaml = Get-Content "k8s/product-deployment.yaml" -Raw
                    $yaml = $yaml.Replace('${IMAGE_TAG}', $env:IMAGE_TAG)

                    [System.IO.File]::WriteAllText(
                        "k8s/product-deployment-rendered.yaml",
                        $yaml,
                        [System.Text.UTF8Encoding]::new($false)
                    )
                '''

                bat 'kubectl apply -f k8s/product-deployment-rendered.yaml'
            }
        }

        // OLD DEPLOYMENT METHOD - Order Service using Kubernetes YAML

        stage('Deploy Order Service') {
            steps {
                powershell '''
                    $yaml = Get-Content "k8s/order-deployment.yaml" -Raw
                    $yaml = $yaml.Replace('${IMAGE_TAG}', $env:IMAGE_TAG)

                    [System.IO.File]::WriteAllText(
                        "k8s/order-deployment-rendered.yaml",
                        $yaml,
                        [System.Text.UTF8Encoding]::new($false)
                    )
                '''

                bat 'kubectl apply -f k8s/order-deployment-rendered.yaml'
            }
        }

        */

        // 15. NEW DEPLOYMENT METHOD - Helm
        stage('Deploy Microservices using Helm') {
            steps {
                bat '"C:\\Users\\Prateek Sangle\\bin\\helm.exe" upgrade --install microservices ./microservices -n microservices-cicd --set product.image.tag=%BUILD_NUMBER% --set order.image.tag=%BUILD_NUMBER% --server-side=false --wait --timeout 180s'
            }
        }

        // 16. Verify Kubernetes Rollout
        stage('Verify Kubernetes Rollout') {
            steps {
                bat 'kubectl rollout status deployment/product-deployment -n microservices-cicd --timeout=180s'
                bat 'kubectl rollout status deployment/order-deployment -n microservices-cicd --timeout=180s'
            }
        }

        // 17. Verify deployed pods
        stage('Verify Kubernetes Pods') {
            steps {
                bat 'kubectl get pods -n microservices-cicd -o wide'
                bat 'kubectl get services -n microservices-cicd'
                bat '"C:\\Users\\Prateek Sangle\\bin\\helm.exe" list -n microservices-cicd'
            }
        }
    }

    post {
        always {
            echo 'CI/CD Pipeline execution completed.'
        }
        success {
            echo 'Both microservices successfully deployed to Kind using Helm!'
        }
        failure {
            echo 'Pipeline failed. Check the failed stage in Console Output.'
        }
    }
}
