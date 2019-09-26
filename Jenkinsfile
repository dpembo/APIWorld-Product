pipeline {
  agent any
  stages {
    stage('Setup') {
      steps {
        sh '''#!/bin/bash

export VERSION="0.0.0CI"
echo ---------------------------------------------------------------------------
echo Build Information
echo ---------------------------------------------------------------------------
echo "Working on : $JOB_NAME"
echo "Workspace  : $WORKSPACE" 
echo "Revision   : $SVN_REVISION"
echo "Build      : $BUILD_NUMBER"
echo ---------------------------------------------------------------------------

rm -rf target/'''
      }
    }
    stage('Build') {
      steps {
        sh '''#build the microservice

docker run --rm --name service-maven -v "$PWD":/usr/share/mymaven -v "$HOME/.m2":/root/.m2 -v "$PWD"/target:/usr/share/mymaven/target -w /usr/share/mymaven maven:3.6-jdk-8 mvn package'''
        sh 'cp $WORKSPACE/target/product-service-0.0.1.jar $WORKSPACE/service.jar'
      }
    }
    stage('Containerize') {
      steps {
        sh '''docker build -t productservice:ci --build-arg PORT=8080 --build-arg JAR_FILE=service.jar .
'''
      }
    }
    stage('Deploy') {
      steps {
        sh '#'
      }
    }
  }
}