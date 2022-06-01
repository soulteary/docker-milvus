# Docker Milvus

![](https://img.shields.io/badge/Ubuntu-20.04-orange) ![](https://img.shields.io/badge/Ubuntu-22.04-orange) ![](https://img.shields.io/badge/OpenBLAS-0.3.9-red) ![](https://img.shields.io/badge/OpenBLAS-0.3.20-red) ![](https://img.shields.io/badge/Docker-latest-blue) 

<img src="images/logo.jpg" width="120"/>

Reliable, highly scalable Milvus docker images.

## [WIP] Docker prebuilt images

**Mirror will be uploaded tomorrow**

I have built some images so far, you can use `docker pull` to get these images directly.

```bash
docker pull ...
```

<table>
<thead>
<tr><th>Docker Image Name</th><th>DockerHub</th><th>Ubuntu</th><th>OpenBLAS</th><th>Golang</th></tr>
</thead><tbody>
<tr><td>soulteary/milvus:base-ubuntu20.04-openblas0.3.9</td><td>Base</td><td>20.04</td><td>0.3.9</td><td>-</td></tr>
<tr><td>soulteary/milvus:base-ubuntu20.04-openblas0.3.20</td><td>Base</td><td>20.04</td><td>0.3.20</td><td>-</td></tr>
<tr><td>soulteary/milvus:base-ubuntu22.04-openblas0.3.9</td><td>Base</td><td>22.04</td><td>0.3.9</td><td>-</td></tr>
<tr><td>soulteary/milvus:base-ubuntu22.04-openblas0.3.20</td><td>Base</td><td>22.04</td><td>0.3.20</td><td>-</td></tr>
<tr><td>soulteary/milvus:builder-ubuntu20.04-openblas0.3.9-golang1.16.9</td><td>builder</td><td>20.04</td><td>0.3.9</td><td>1.16.9</td></tr>
<tr><td>soulteary/milvus:builder-ubuntu20.04-openblas0.3.20-golang1.16.9</td><td>builder</td><td>20.04</td><td>0.3.20</td><td>1.16.9</td></tr>
<tr><td>soulteary/milvus:builder-ubuntu22.04-openblas0.3.9-golang1.16.9</td><td>builder</td><td>22.04</td><td>0.3.9</td><td>1.16.9</td></tr>
<tr><td>soulteary/milvus:builder-ubuntu22.04-openblas0.3.20-golang1.16.9</td><td>builder</td><td>22.04</td><td>0.3.20</td><td>1.16.9</td></tr>
<tr><td>soulteary/milvus:ubuntu20.04-openblas0.3.9-golang1.16.9</td><td>app</td><td>20.04</td><td>0.3.9</td><td>1.16.9</td></tr>
<tr><td>soulteary/milvus:ubuntu20.04-openblas0.3.20-golang1.16.9</td><td>app</td><td>20.04</td><td>0.3.20</td><td>1.16.9</td></tr>
<tr><td>soulteary/milvus:ubuntu22.04-openblas0.3.9-golang1.16.9</td><td>app</td><td>22.04</td><td>0.3.9</td><td>1.16.9</td></tr>
<tr><td>soulteary/milvus:ubuntu22.04-openblas0.3.20-golang1.16.9</td><td>app</td><td>22.04</td><td>0.3.20</td><td>1.16.9</td></tr>
</tbody></table>

## How to build

If you need to build locally, you can refer to the following steps.
### 1. Build base image

Before building milvus, we first need to build a base image that includes openblas (3.9+)

```bash
docker build -t soulteary/milvus:base-ubuntu20.04-openblas0.3.9 -f docker/base/Dockerfile .
```

[Advanced usage](./docs/01.build-openblas.md)

### 2. Build Milvus tools image

When we prepare the base image, we need to build a tool image that includes c++ and golang to build milvus in it.

```bash
docker build -t soulteary/milvus:builder-ubuntu20.04-openblas0.3.9 -f docker/builder/Dockerfile .
```

By default, we will get the latest Milvus code from GitHub, you can get the code from other data sources by adjusting the build parameters. [Advanced usage](./docs/02.build-builder.md)

### 3. Build a tiny Milvus Application image

In the previous build, we have solved the Milvus and related software dependencies that can run normally, so we only need a simple command to generate an image that is easy to transfer.

```bash
docker build -t soulteary/milvus:ubuntu20.04-openblas0.3.9 -f docker/app/Dockerfile .
```

### [WIP] 4. Build a Milvus Application image with debugger

TBD, how to build an image that supports remote debugging.


## PLAN

- [ ] Complete WIP content.
- [ ] Try privatized Action Runner for efficient automated builds.
- [ ] Add TARBALL source code build method.
- [ ] Add an environment that is convenient for golang developers to use.
- [ ] Improve the build of debug images
- [ ] Build Milvus images **in batches**, use openblas 0.3.9-0.3.20, ubuntu 20.04-22.04, golang 1.16-1.18 to build
