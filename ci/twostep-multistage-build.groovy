pipeline {
  agent {
    kubernetes {
      inheritFrom "default"
      yaml '''
        apiVersion: v1
        kind: Pod
        metadata:
          labels:
            com.joyrex2001.kubedock.runas-user: 1000
        spec:
          containers:
            - name: jnlp
              securityContext:
                runAsUser: 1000
            - name: maven
              workingDir: /home/jenkins/agent
              image: maven:3.9.3-eclipse-temurin-17
              command:
                - sleep
              args:
                - 99d
              securityContext:
                runAsUser: 1000
            - name: buildah
              workingDir: /home/jenkins/agent
              image: niiku/buildah-rootless:latest
              command:
                - sleep
              args:
                - 99d
              securityContext:
                runAsUser: 1000
        '''
    }
  }
  stages {
    stage('Maven build') {
      steps {
        container('maven') {
          echo POD_CONTAINER // displays 'maven'
         
          dir('examples/hello-quarkus') {
            sh '''
               mvn clean package -Dquarkus.package.type=native-sources -Dmaven.repo.local=/home/jenkins/agent/.m2
               '''
          }
        }
      }
    }
    stage('Native Image Build') {
      steps {
        container('buildah') {
          echo POD_CONTAINER // displays 'buildah'
         
          dir('examples/hello-quarkus') {
            sh '''
               buildah bud -t hello-quarkus:latest -f ./src/main/docker/Dockerfile.twostep-multistage
               '''
          }
        }
      }
    }
  }
}
