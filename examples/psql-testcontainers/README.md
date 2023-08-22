# Testcontainers with Postgres

## Bridge Docker API <> K8S API 

To be able to use testcontainers the system must have a Docker Engine, or
compatible system, over TCP available. [KubeDock](https://github.com/joyrex2001/kubedock)
provides a Docker API and calls internally the K8S API and creates
corresponding resources. A docker client is not necessary as
[testcontainers](https://testcontainers.com) calls the Docker API directly but
the _engine_ must be exposed over the environment variable `DOCKER_HOST`

Additional two features in testcontainers have to be disabled because they do
not work correctly on K8S. The startup check `TESTCONTAINERS_CHECKS_DISABLE=true`
and the cleanup task `TESTCONTAINERS_RYUK_DISABLED=true`.


## Bitnami Image

OpenShift has some constraints on security contexts and allowed user IDs inside
containers. This is why the original [postgres image](https://hub.docker.com/_/postgres)
will not work. At least not without serious tinkering.

One alternative is the [bitnami postgres image](https://hub.docker.com/r/bitnami/postgresql)
which has several difference compared to the original:

* Env vars are named differently, here the most important
  * `POSTGRESQL_DATABASE` instead of `POSTGRES_DB`
  * `POSTGRESQL_USERNAME` instead of `POSTGRES_USER`
  * `POSTGRESQL_PASSWORD` instead of `POSTGRES_PASSWORD`
* Log output is different and shows only parts
  * Especially the double startup of postgres is hidden and only the second
    _ready_ message is displayed

## Custom testcontainers container

Because of the mani small differences between bitnami and the original image it
is a good idea to provide a custom implementation for the container.
