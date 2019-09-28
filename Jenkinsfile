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
'''
        sh '''echo "Clean Microservice Containers"
runningCount=`docker ps -a -q --filter ancestor=productservice:0 | wc -l`

if [ $runningCount -gt 0 ]; then
   docker stop $(docker ps -a -q --filter ancestor=productservice:0 --format="{{.ID}}") > /dev/nul
else
   echo "No MS Containers running"
fi

#Stop containers
docker stop productmg
docker stop productservicems

echo "Clean Test Containers"


sleep 1

runningCount=`docker ps -a -q --filter ancestor=jmeter:latest | wc -l`

if [ $runningCount -gt 0 ]; then
   docker stop $(docker ps -a -q --filter ancestor=productservice:0 --format="{{.ID}}") > /dev/nul
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
        sh '''#Build MicroGateway
cd /opt/softwareag/microgateway
./microgateway.sh createDockerFile --docker_dir . -p 9090 -a $WORKSPACE/microgateway/Product.zip -dof ./Dockerfile -c $WORKSPACE/microgateway/aliases-build.yml'''
      }
    }
    stage('Containerize') {
      steps {
        sh '''#Containerize Microservice
docker build -t productservice:ci --build-arg PORT=8090 --build-arg JAR_FILE=service.jar .
'''
        sh '''#Containerize Microgateway
cd /opt/softwareag/microgateway
docker build -t productmg:ci .
'''
      }
    }
    stage('Deployment') {
      parallel {
        stage('Start MicroSvc') {
          steps {
            sh '''#Run the container read for testing

docker run --rm --name productservicems -d -p 8090:8090 productservice:ci
'''
          }
        }
        stage('Start MicroGW') {
          steps {
            sh '''#Run MicroGateway Container
docker run --rm --name productmg -d -p 9090:9090 productmg:ci
'''
          }
        }
      }
    }
    stage('Testing') {
      parallel {
        stage('Load Test') {
          steps {
            sh '''rm -rf jmeter
mkdir jmeter
mkdir jmeter/output
cp src/main/loadtest.jmx jmeter/
docker run --rm --name jmeter --volume $WORKSPACE/jmeter/:/mnt/jmeter vmarrazzo/jmeter:latest -n -t /mnt/jmeter/loadtest.jmx -l /mnt/jmeter/result.jtl -j /mnt/jmeter/result.log -e -o /mnt/jmeter/output'''
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
        sh '''#push image to registry

#First tag
docker tag productservice:ci apiworldref:5000/productservice
docker tag productmg:ci apiworldref:5000/productmg

#second push 
docker push apiworldref:5000/productservice
docker push apiworldref:5000/productmg'''
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
    stage('Tidyup') {
      steps {
        sh '''#Tidy up after build

#Stop containers
docker stop productmg
docker stop productservicems

#Prune
docker image prune -f
docker volume prune -f

'''
      }
    }
    stage('Complete') {
      steps {
        echo 'Complete'
      }
    }
  }
}