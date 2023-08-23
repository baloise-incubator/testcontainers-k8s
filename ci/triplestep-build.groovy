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
            - name: mandrel
              workingDir: /home/jenkins/agent
              image: quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17
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
  parameters {
    string(name: 'PROJECT', defaultValue: 'examples/quarkus-native', description: 'Project to build')
    string(name: 'IMAGE', defaultValue: 'quarkus-native:latest', description: 'Image name incl. tag')
    string(name: 'DOCKERFILE', defaultValue: 'Dockerfile.triplestep', description: 'Name of Dockerfile')
  }
  stages {
    stage('Maven build') {
      steps {
        container('maven') {
          dir(params.PROJECT) {
            sh '''mvn clean package \
              -Dquarkus.package.type=native-sources \
              -Dmaven.repo.local=$(pwd)/.m2'''
          }
        }
      }
    }
    stage('Native Build') {
      steps {
        container('mandrel') {
          dir("${params.PROJECT}/target/native-sources") {
            sh 'native-image $(cat native-image.args)'
          }
        }
      }
    }
    stage('Docker Image Build') {
      steps {
        container('buildah') {
          dir(params.PROJECT) {
            sh "buildah bud -t ${params.IMAGE} -f ./src/main/docker/${params.DOCKERFILE}"
          }
        }
      }
    }
  }
}
