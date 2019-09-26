pipeline {
  agent any
  stages {
    stage('Setup') {
      steps {
        sh '''export VERSION="0.0.0CI"

#!/bin/bash
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
        sh 'cp target/product-service-0.0.1.jar ../service.jar'
      }
    }
  }
}