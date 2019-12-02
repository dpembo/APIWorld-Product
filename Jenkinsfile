podTemplate(
    label: 'mypod', 
    inheritFrom: 'default',
    containers: [
        containerTemplate(
            name: 'maven', 
            image: 'maven:3.6-jdk-8',
            ttyEnabled: true,
            command: 'cat'
        ),
        containerTemplate(
            name: 'docker', 
            image: 'docker:18.02',
            securityContext:
              runAsUser: 0
              fsGroup: 0
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
            steps {
              echo 'Clean Microservice Containers'
            }
          }
          stage('Get Version Number') {
            steps {
              echo 'Get Version Number'
              load 'versionInput.groovy'
            }
          }
        }
      }
    }
}