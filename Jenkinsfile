pipeline {
    agent none

    environment {
        REPO_URL       = 'https://github.com/mhykari/jenkins-java-game-api.git'
        PROJECT_DIR    = 'guess-the-number'
        IMAGE_NAME     = 'guess-number-api'
        CONTAINER_NAME = 'guess-number-api-container'
    }

    stages {

        stage('Checkout Code') {
            agent any
            steps {
                echo "Cloning repository..."
                git branch: 'main', url: "${REPO_URL}"
            }
        }

        stage('SonarQube Analysis') {
            agent any
            steps {
                withSonarQubeEnv('sonar') {   // sonar = name in Jenkins Configure System
                    script {
                        def scannerHome = tool 'sonar'   // <--- FIX
                        dir("${PROJECT_DIR}") {
                            sh """
                                ${scannerHome}/bin/sonar-scanner \
                                  -Dsonar.projectKey=guess-the-number \
                                  -Dsonar.sources=src/main/java \
                                  -Dsonar.java.binaries=target/classes \
                                  -Dsonar.host.url=http://192.168.43.212:9000
                            """
                        }
                    }
                }
            }
        }

        stage('Build with Maven') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-21-alpine'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                dir("${PROJECT_DIR}") {
                    echo "Building project with Maven..."
                    sh 'mvn clean package -DskipTests'
                    stash includes: 'target/*.jar', name: 'jarFile'
                }
            }
        }

        stage('Build Docker Image') {
            agent any
            steps {
                dir("${PROJECT_DIR}") {
                    echo "Building Docker image..."
                    unstash 'jarFile'
                    sh '''
                        export DOCKER_BUILDKIT=1
                        docker build -t ${IMAGE_NAME}:latest .
                    '''
                }
            }
        }

        stage('Deploy with Docker Compose') {
            agent any
            steps {
                dir("${PROJECT_DIR}") {
                    echo "Creating docker-compose.yml dynamically..."

                    writeFile file: 'docker-compose.yml', text: """
version: '3.8'

services:
  guess-number-api:
    image: ${IMAGE_NAME}:latest
    container_name: ${CONTAINER_NAME}
    ports:
      - '8085:8080'
    restart: unless-stopped
"""

                    echo "Deploying with Docker Compose..."
                    sh '''
                        docker compose down || true
                        docker compose up -d --build
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Guess-the-number API deployed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check logs.'
        }
        always {
            script {
                node {
                    cleanWs()
                }
            }
        }
    }
}
