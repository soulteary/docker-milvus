# Docker Milvus

## Build by yourself

1. Build a base images, contains openblas 3.9+

```bash
docker build -t openblas -f Dockerfile.base .
```

extra support build args:

```bash
# use tsinghua mirror
--build-arg=USE_MIRROR=true
# specify a version
--build-arg=OPENBLAS=0.3.9
```
