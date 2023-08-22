pipeline {
  agent {
    kubernetes {
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
                - "./kubedock"
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
        '''
    }
  }
  stages {
    stage('Run maven') {
      steps {
        container('maven') {
          echo POD_CONTAINER // displays 'maven'
          // sh 'git clone https://github.com/baloise-incubator/testcontainers-k8s.git'

          dir('robert') {
            sh '''export TESTCONTAINERS_RYUK_DISABLED=true && \
                  export TESTCONTAINERS_CHECKS_DISABLE=true && \
                  export DOCKER_HOST=tcp://localhost:2475 && \
                  mvn test -Dmaven.repo.local=/home/jenkins/agent/.m2'''
          }
        }
      }
    }
  }
}
