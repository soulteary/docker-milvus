FROM ubuntu:20.04
LABEL maintainer=soulteary@gmail.com

ARG USE_MIRROR=false
RUN if [ "$USE_MIRROR" = true ] ; then sed -i -e "s/archive.ubuntu.com/mirrors.tuna.tsinghua.edu.cn/" /etc/apt/sources.list && sed -i -e "s/security.ubuntu.com/mirrors.tuna.tsinghua.edu.cn/" /etc/apt/sources.list; fi

ENV DEBIAN_FRONTEND noninteractive

# deps
RUN apt-get update && \
    # common utils for download sources tarball/zipball
    apt-get install -y --no-install-recommends curl wget ca-certificates gnupg2 && \
    # openblas deps
    apt-get install -y --no-install-recommends g++ gcc gfortran git make && \
    # cleanup
    apt-get remove --purge -y && rm -rf /var/lib/apt/lists/*

# build openblas 3.8+ from source https://github.com/xianyi/OpenBLAS
ARG OPENBLAS=0.3.9
ENV OPENBLASH_VERSION=${OPENBLAS}
RUN wget https://github.com/xianyi/OpenBLAS/archive/v${OPENBLASH_VERSION}.tar.gz && \
    tar zxvf v${OPENBLASH_VERSION}.tar.gz && \
    cd OpenBLAS-${OPENBLASH_VERSION} && \
    # same complie options as milvus 18.04 dockerfile
    make TARGET=CORE2 DYNAMIC_ARCH=1 DYNAMIC_OLDER=1 USE_THREAD=0 USE_OPENMP=0 FC=gfortran CC=gcc COMMON_OPT="-O3 -g -fPIC" FCOMMON_OPT="-O3 -g -fPIC -frecursive" NMAX="NUM_THREADS=128" LIBPREFIX="libopenblas" LAPACKE="NO_LAPACKE=1" INTERFACE64=0 NO_STATIC=1 && \
    make -j4 PREFIX=/usr NO_STATIC=1 install && \
    cd .. && rm -rf OpenBLAS-${OPENBLASH_VERSION} && rm v${OPENBLASH_VERSION}.tar.gz
