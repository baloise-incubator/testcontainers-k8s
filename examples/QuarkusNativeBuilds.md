# Native Build for jenkins devops-stack

# Jenkins for k8s-quark-ci

https://k8s-quark-ci.baloise.dev/

# Requirements

* Runs with jenkins-templated Docker buildcontainers
* Runs in Openshift
* Zero or Minimal Adjustments for generated Quarkus Dockerfiles

# Variants

## Buildah Multistage Dockerfile

1) Runs one step in Jenkins, running Buildah Build container.
2) Uses the quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17 builder-image.
3) Copies [mavenwrapper](./quarkus-native/.mvn/wrapper) into the build-container, makes it executable.
4) Starts maven-build with native compile argument.
5) Build Dockerimage in last Dockerfile Stage.

* see [Pipeline](../ci/multistage-build.groovy)
* see [Dockerfile](./quarkus-native/src/main/docker/Dockerfile.multistage)

## Two-Step Pipeline with Multistage Dockerbuild

1) Runs two steps in Jenkins, running
    a) Maven Build container.
    b) Mandrel Build container.
2) Uses the quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17 builder-image.
3) Starts maven-build with native-sources compile argument. This stage just prepares the native-compile build with java-binaries and native-image.args.
4) Starts mandrel-build taking native-sources
    a) compiles native executable.
    b) builds Dockerimage in last Dockerfile Stage.

* see [Pipeline](../ci/twostep-multistage-build.groovy)
* see [Dockerfile](./quarkus-native/src/main/docker/Dockerfile.twostep-multistage)

## Triple-Step Pipeline (_preferred_)

Runs three steps in Jenkins, running
    a) Maven Build container.
    b) Mandrel Build container.
    c) Buildah Container-Build.

1) Starts maven-build with native-sources compile argument. This stage just prepares the native-compile build with java-binaries and native-image.args.
2) Starts mandrel-build (quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17 builder-image) taking native-sources and compiles the natvie executable.
3) Builds Dockerimage in buildah buildcontainer.

* see [Pipeline](../ci/triplestep-build.groovy)
* see [Dockerfile](./quarkus-native/src/main/docker/Dockerfile.triplestep)

## Buildah Triplestage

Runs one Buildah-Step in Jeknins.
the used Dockerfile consists of three Build-Stages:
1) From Maven-Image, build maven with java-binaries and native-image.args.
2) From Mandrel-Image, compile native executable
3) From Distro-Image, build dockerimage.

* see [Pipeline](../ci/buildah-triplestage.groovy)
* see [Dockerfile](./quarkus-native/src/main/docker/Dockerfile.buildah-triplestage)
