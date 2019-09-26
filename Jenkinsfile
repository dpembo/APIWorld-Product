pipeline {
  agent any
  stages {
    stage('Setup') {
      steps {
        sh 'export VERSION="0.0.0CI"'
      }
    }
    stage('Build') {
      steps {
        sh '''#build the microservice

docker run --rm --name service-maven -v "$PWD":/usr/share/mymaven -v "$HOME/.m2":/root/.m2 -v "$PWD"/target:/usr/share/mymaven/target -w /usr/share/mymaven maven:3.6-jdk-8 mvn package'''
      }
    }
  }
}