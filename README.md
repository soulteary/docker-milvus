# Docker Milvus

## 1. Build base image

Before building milvus, we first need to build a base image that includes openblas (3.9+)

```bash
docker build -t soulteary/milvus-base:ubuntu20.04-openblas3.9 -f docker/base/Dockerfile .
```

[Advanced usage](./docs/01.build-openblas.md)

## 2. Build Milvus tools image

Build a milvus tools image, contains c++/golang develpment tools to build milvus app.

Install deps and download milvus latest sources from github:

```bash
docker build -t soulteary/milvus-builder:ubuntu20.04-openblas3.9 -f docker/builder/Dockerfile .
```

extra support build args:

```bash
# Specify the source of the software source code
MILVUS_GIT_REPO=https://github.com/milvus-io/milvus.git
MILVUS_GIT_BRANCH=master
MILVUS_GIT_CLONE_DEPTH=
# Custom go version, binary download mirror, package download mirror
GOLANG_VERSION=1.16.9
GO_BINARY_BASE_URL=https://golang.google.cn/dl/
GOPROXY_URL=https://proxy.golang.org
```

If you want to speed up the build and want to specify some options:

```bash
docker build \
    --build-arg=MILVUS_GIT_REPO=https://gitee.com/milvus-io/milvus.git \
    --build-arg=GOPROXY_URL=https://goproxy.cn \
    -t soulteary/milvus-builder:ubuntu20.04-openblas3.9 -f docker/builder/Dockerfile .
```

If you encounter a situation where you cannot download dependencies because the network is not smooth, you can solve the problem by setting a proxy network:

```bash
docker build \
    --build-arg=http_proxy=http://10.11.12.90:8001 \
    --build-arg=https_proxy=http://10.11.12.90:8001 \
    --build-arg=MILVUS_GIT_REPO=https://gitee.com/milvus-io/milvus.git \
    --build-arg=GOPROXY_URL=https://goproxy.cn \
    -t soulteary/milvus-builder:ubuntu20.04-openblas3.9 -f docker/builder/Dockerfile .
```
