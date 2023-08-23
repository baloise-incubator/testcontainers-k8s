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
            sh '''buildah bud -t hello-quarkus:latest -f ./src/main/docker/Dockerfile.buildah-triplestage
               '''
          }
        }
      }
    }
  }
}
