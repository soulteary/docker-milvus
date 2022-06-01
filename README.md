# Docker Milvus

![](https://img.shields.io/badge/Ubuntu-20.04-orange) ![](https://img.shields.io/badge/Ubuntu-22.04-orange) ![](https://img.shields.io/badge/OpenBLAS-0.3.9-red) ![](https://img.shields.io/badge/OpenBLAS-0.3.20-red) ![](https://img.shields.io/badge/Docker-latest-blue) 

<img src="images/logo.jpg" width="120"/>

Reliable, highly scalable Milvus docker images.

## [WIP] Docker prebuilt images

I have built some images so far, you can use `docker pull` to get these images directly.

```bash
docker pull ...
```

## How to build

If you need to build locally, you can refer to the following steps.
### 1. Build base image

Before building milvus, we first need to build a base image that includes openblas (3.9+)

```bash
docker build -t soulteary/milvus-base:ubuntu20.04-openblas0.3.9 -f docker/base/Dockerfile .
```

[Advanced usage](./docs/01.build-openblas.md)

### 2. Build Milvus tools image

When we prepare the base image, we need to build a tool image that includes c++ and golang to build milvus in it.

```bash
docker build -t soulteary/milvus-builder:ubuntu20.04-openblas0.3.9 -f docker/builder/Dockerfile .
```

By default, we will get the latest Milvus code from GitHub, you can get the code from other data sources by adjusting the build parameters. [Advanced usage](./docs/02.build-builder.md)

### [WIP] 3. Build a tiny Milvus Application image

TBD

### [WIP] 4. Build a Milvus Application image with debugger

TBD