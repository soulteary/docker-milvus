# Docker Milvus

## Build by yourself

1. Build a base images, contains openblas 3.9+

```bash
docker build -t soulteary/milvus-base:ubuntu20.04-openblas3.9 -f Dockerfile.base .
```

extra support build args:

```bash
# use tsinghua mirror
--build-arg=USE_MIRROR=true
# specify a version
--build-arg=OPENBLAS=0.3.9
```

If you want to speed up the build and want to specify the version:

```bash
docker build --build-arg=USE_MIRROR=true --build-arg=OPENBLAS=0.3.9 -t soulteary/milvus-base:ubuntu20.04-openblas3.9 -f Dockerfile.base .
```
