podTemplate(
    label: 'mypod', 
    inheritFrom: 'default',
    containers: [
        containerTemplate(
            name: 'maven', 
            image: 'maven:3.6-jdk-8',
            ttyEnabled: true,
            command: 'cat'
        )
    ],
    volumes: [
        hostPathVolume(
            hostPath: '/var/run/docker.sock',
            mountPath: '/var/run/docker.sock'
        )
    ]
) {
    node('mypod') {
        stage('Setup') {
          parallel {
            stage('Setup') {
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
echo "GIT_COMMIT : $GIT_COMMIT" 
echo "GIT_BRANCH : $GIT_BRANCH"
echo ---------------------------------------------------------------------------
'''
              sh '''echo "Clean Microservice Containers"
'''
            }
            stage('Get Version Number') {
              steps {
                echo 'Get Version Number'
                load 'versionInput.groovy'
              }
            }
          }
        }
        stage('Build') {
          container ('maven') {
          steps {
            echo 'Build Project'
            sh '''if [[ -z "$VERSION" ]]; then
    VERSION=ci
  fi
  echo Version is: $VERSION

  #CompileTest Microservice
  echo "Compile Microservice"
  mvn compile
  mvn package
  '''
          }
        }
      }
    }
}