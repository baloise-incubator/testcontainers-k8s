# Native Image Build

see [Description in robert subproject](robert/NativeBuild.md)

# testcontainers-k8s

## Is the use case solved upstream?
No  -> https://java.testcontainers.org/

## Possible Solutions

### Adapter

* https://github.com/joyrex2001/kubedock

### Extend Test-Containers

* https://github.com/testcontainers/testcontainers-java/issues/1135


### Alternative Framework

* https://github.com/JeanBaptisteWATENBERG/junit5-kubernetes

## Working Solution with Quarkus and PostgreSQL Testcontainer

see [Detailed documentation](examples/PostgreSQLTestcontainer.md)

## Resources

Our fork of [testcontainers-java](https://github.com/baloise-incubator/testcontainers-java)

## Links

- https://timmhirsens.de/posts/2019/07/testcontainers_on_jenkins_with_kubernetes/
- https://jenkins-x.io/blog/2021/05/17/dont-use-docker/
- https://github.com/testcontainers/testcontainers-java/issues/449 
