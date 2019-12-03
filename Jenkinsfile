pipeline {
  agent {
    kubernetes {
      label 'myPod'
      yaml """
  kind: Pod
  metadata:
    labels:
      app: test
  spec:
    securityContext:
      runAsUser: 1724
      fsGroup: 1724
    containers:
    - name: mg-jenkins
      image: docker.devopsinitiative.com/mg-jenkins:10.5
      command:
      - cat
      tty: true
    - name: maven
      image: maven:3.3.9-jdk-8-alpine
      command:
      - cat
      tty: true
    - name: docker
      image: docker:18.02
      securityContext:
        runAsUser: 0
        fsGroup: 0
      command:
      - cat
      tty: true
      volumeMounts:
      - mountPath: /var/run/docker.sock
        name: docker-sock-volume
    volumes:
    - name: docker-sock-volume
      hostPath:
        # location on host
        path: /var/run/docker.sock
        # this field is optional
        type: File
    imagePullSecrets:
    - name: regcred
  """
    }
  }
  stages {
        stage('Checkout') {
          steps {
            echo "Chekout"
            echo sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
          }
        }
        stage('Build') {
          steps {
            container('maven') {
              echo 'Build Project'
              sh '''
  if [[ -z "$VERSION" ]]; then
    VERSION=ci
  fi
  echo Version is: $VERSION
  '''
              echo "Compile Microservice"
              sh 'mvn compile'
              echo "Package the Microservice"
              sh 'mvn package'
              sh '''
#Modify Alias depending on stage

if [ $GIT_BRANCH = "staging" ]; then
   sed -i \'s/\\[gateway\\]/apiworldref\\:5555/g\' microgateway/config.yml
   sed -i \'s/\\[microservice\\]/localhost\\:8090/g\' microgateway/config.yml
   exit
fi

if [ $GIT_BRANCH = "master" ]; then
   sed -i \'s/\\[gateway\\]/apiworldref\\:5555/g\' microgateway/config.yml
   sed -i \'s/\\[microservice\\]/localhost\\:8090/g\' microgateway/config.yml   
   exit
fi

#Else assume its a development branch and set accordingly

sed -i \'s/\\[gateway\\]/apiworldbuild\\:5555/g\' microgateway/config.yml
sed -i \'s/\\[microservice\\]/apiworldbuild\\:8090/g\' microgateway/config.yml

'''
            }
          }
        }
        stage('Build') {
          steps {
            container('mg-jenkins') {
              echo 'Build Project'
              sh '''
              WORKSPACE=`pwd`
cd /opt/softwareag/microgateway
./microgateway.sh createDockerFile --docker_dir . -p 9090 -a $WORKSPACE/microgateway/Product.zip -dof ./Dockerfile -c $WORKSPACE/microgateway/config.yml'''
'''
            }
          }
        }
        stage('Parallel Stage') {
            when {
                branch 'master'
            }
            failFast true
            parallel {
                stage('Branch A') {
                    steps {
                        echo "On Branch A"
                    }
                }
                stage('Branch B') {
                    steps {
                        echo "On Branch B"
                    }
                }
                stage('Branch C') {
                    stages {
                        stage('Nested 1') {
                            steps {
                                echo "In stage Nested 1 within Branch C"
                            }
                        }
                        stage('Nested 2') {
                            steps {
                                echo "In stage Nested 2 within Branch C"
                            }
                        }
                    }
                }
            }
        }
    }
}