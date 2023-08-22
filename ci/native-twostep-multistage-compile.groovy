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
            - name: kubedock
              image: joyrex2001/kubedock:0.12.0
              command:
                - "/app/kubedock"
              args:
                - server
                - '--runas-user'
                - '1001130000'
                - '--reverse-proxy'
              securityContext:
                runAsUser: 1000
              ports:
                - containerPort: 2475
              env:
                - name: SERVICE_ACCOUNT
                  value: kubedock    
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
         
          dir('robert') {
            sh '''export TESTCONTAINERS_RYUK_DISABLED=true && \
                  export TESTCONTAINERS_CHECKS_DISABLE=true && \
                  export DOCKER_HOST=tcp://localhost:2475 && \
                  mvn clean package -Dquarkus.package.type=native-sources -Dmaven.repo.local=/home/jenkins/agent/.m2'''
          }
        }
      }
    }
    stage('Native Image Build') {
      steps {
        container('buildah') {
          echo POD_CONTAINER // displays 'buildah'
         
          dir('robert') {
            sh '''
               buildah bud -t robert:latest -f ./src/main/docker/Dockerfile.native-multistage2
               '''
          }
        }
      }
    }
  }
}