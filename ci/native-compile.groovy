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
            - name: buildah
              workingDir: /home/jenkins/agent
              image: niiku/buildah-rootless:latest
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
        '''
    }
  }
  stages {
    stage('Run maven') {
      steps {
        container('buildah') {
          echo POD_CONTAINER // displays 'maven'
          // sh 'git clone https://github.com/baloise-incubator/testcontainers-k8s.git'

          dir('examples/hello-quarkus') {
            sh '''export TESTCONTAINERS_RYUK_DISABLED=true && \
                  export TESTCONTAINERS_CHECKS_DISABLE=true && \
                  export DOCKER_HOST=tcp://localhost:2475 && \
                  export PGDATA=/home/jenkins/agent/workspace/.m2/pg-data && mkdir -p $PGDATA && \
                  buildah bud -t hello-quarkus:latest -f ./src/main/docker/Dockerfile.native-multistage
               '''
          }
        }
      }
    }
  }
}
