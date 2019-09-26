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
        sh '''echo "CLEAN UP"
runningCount=`docker ps -a -q --filter ancestor=productservice:0 | wc -l`

if [ $runningCount -gt 0 ]; then
   docker rm $(docker stop $(docker ps -a -q --filter ancestor=productservice:0 --format="{{.ID}}")) > /dev/nul
else
   echo "No Containers running"
fi
'''
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
        sh '''#Run the container read for testing

docker run -d -p 8090:8090 productservice:0
'''
      }
    }
    stage('Load Test') {
      steps {
        sh '''rm -rf jmeter
mkdir jmeter
mkdir jmeter/output
cp src/main/loadtest.jmx jmeter/
sudo docker run --volume /var/lib/jenkins/workspace/APIWorld-Product_master/jmeter/:/mnt/jmeter vmarrazzo/jmeter:latest -n -t /mnt/jmeter/loadtest.jmx -l /mnt/jmeter/result.jtl -j /mnt/jmeter/result.log -e -o /mnt/jmeter/output'''
        perfReport(sourceDataFiles: 'jmeter/result.jtl', compareBuildPrevious: true, errorUnstableResponseTimeThreshold: '5000')
      }
    }
    stage('Release To Test') {
      when {
        anyOf {
          environment name: 'DEPLOY_TO', value: 'staging'
        }

      }
      steps {
        echo 'Release to test'
      }
    }
    stage('Done') {
      steps {
        echo 'done'
      }
    }
  }
  environment {
    DEPLOY_TO = 'staging'
  }
}