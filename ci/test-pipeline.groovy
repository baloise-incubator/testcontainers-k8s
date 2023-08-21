podTemplate(yaml: '''
    apiVersion: v1
    kind: Pod
    spec:
      containers:
        - name: jnlp
          securityContext:
            runAsUser: 1000
        - name: maven
          image: maven:3.8.1-jdk-8
          command:
            - sleep
          args:
            - 99d
          securityContext:
            runAsUser: 1000
        - name: kubedock
          image: joyrex2001/kubedock:0.12.0
          command:
            - kubedock server --runas-user 1000
          args:
            - 99d
          securityContext:
            runAsUser: 1000
          ports:
            containerPort: 2475
    ''') {
    node(POD_LABEL) {
      container('maven') {
        echo POD_CONTAINER // displays 'maven'
        sh 'hostname'

        // sh 'export TESTCONTAINERS_RYUK_DISABLED=true'
        // sh 'export TESTCONTAINERS_CHECKS_DISABLE=true'
        // sh 'export DOCKER_HOST=tcp://127.0.0.1:2475'
        // sh 'mvn test'
      }
    }
}