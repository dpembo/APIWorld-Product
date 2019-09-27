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
echo "Deploy to  : $DEPLOY_TO"
echo ---------------------------------------------------------------------------

rm -rf target/'''
        sh '''echo "Clean Microservice Containers"
runningCount=`docker ps -a -q --filter ancestor=productservice:0 | wc -l`

if [ $runningCount -gt 0 ]; then
   docker rm $(docker stop $(docker ps -a -q --filter ancestor=productservice:0 --format="{{.ID}}")) > /dev/nul
else
   echo "No MS Containers running"
fi

echo "Clean Test Containers"
runningCount=`docker ps -a -q --filter ancestor=jmeter:latest | wc -l`

if [ $runningCount -gt 0 ]; then
   docker rm $(docker stop $(docker ps -a -q --filter ancestor=productservice:0 --format="{{.ID}}")) > /dev/nul
else
   echo "No Jmeter Containers running"
fi

echo "Clean Build Assets"
rm -rf target
rm -rf jmeter
'''
      }
    }
    stage('Build') {
      steps {
        sh '''#CompileTest Microservice
echo "Compile Microservice"
docker run --rm --name service-maven -v "$PWD":/usr/share/mymaven -v "$HOME/.m2":/root/.m2 -v "$PWD"/target:/usr/share/mymaven/target -w /usr/share/mymaven maven:3.6-jdk-8 mvn compile'''
        sh '''#Package the microservice
echo "Package the Microservice"
docker run --rm --name service-maven -v "$PWD":/usr/share/mymaven -v "$HOME/.m2":/root/.m2 -v "$PWD"/target:/usr/share/mymaven/target -w /usr/share/mymaven maven:3.6-jdk-8 mvn package'''
        sh '''echo "Move Package for Docker Build"
cp $WORKSPACE/target/product-service-0.0.1.jar $WORKSPACE/service.jar'''
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
      parallel {
        stage('Load Test') {
          steps {
            sh '''rm -rf jmeter
mkdir jmeter
mkdir jmeter/output
cp src/main/loadtest.jmx jmeter/
docker run --volume $WORKSPACE/jmeter/:/mnt/jmeter vmarrazzo/jmeter:latest -n -t /mnt/jmeter/loadtest.jmx -l /mnt/jmeter/result.jtl -j /mnt/jmeter/result.log -e -o /mnt/jmeter/output'''
            perfReport(sourceDataFiles: 'jmeter/result.jtl', compareBuildPrevious: true, errorUnstableResponseTimeThreshold: '5000')
          }
        }
        stage('Unit Test') {
          steps {
            sh '''#Unit Test Microservice
echo "Unit Test Microservice"
docker run --rm --name service-maven -v "$PWD":/usr/share/mymaven -v "$HOME/.m2":/root/.m2 -v "$PWD"/target:/usr/share/mymaven/target -w /usr/share/mymaven maven:3.6-jdk-8 mvn test'''
          }
        }
      }
    }
    stage('Release To Test') {
      when {
        anyOf {
          branch 'staging'
          branch 'master'
        }

      }
      steps {
        echo 'Release to test'
      }
    }
    stage('Release To Production') {
      when {
        branch 'master'
      }
      steps {
        echo 'Release to Prod'
      }
    }
    stage('Done') {
      steps {
        echo 'done'
      }
    }
  }
}