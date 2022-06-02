# FROM soulteary/milvus:base-ubuntu20.04-openblas0.3.9
ARG BASE_IMAGE
FROM ${BASE_IMAGE}

LABEL maintainer=soulteary@gmail.com

# build cmake 3.16.3+ from source https://cmake.org
ENV DOCKER_CMAKE_TARBALL=https://cmake.org/files/v3.18/cmake-3.18.6-Linux-x86_64.tar.gz
RUN wget -qO- "${DOCKER_CMAKE_TARBALL}" | tar --strip-components=1 -xz -C /usr/local

# install linux build deps (from milvus/scripts/install_deps.sh)
RUN apt update && \
    apt-get install -y g++ gcc make lcov libtool m4 autoconf automake ccache libssl-dev zlib1g-dev libboost-regex-dev libboost-program-options-dev libboost-system-dev libboost-filesystem-dev libboost-serialization-dev python3-dev libboost-python-dev libcurl4-openssl-dev gfortran libtbb-dev \
    # install extra deps `clang-format-10 clang-tidy-10` (from milvus/build/docker/builder/cpu/ubuntu20.04/Dockerfile)
    clang-format-10 clang-tidy-10 \
    # install extra deps for golang installer
    bison && \
    # cleanup
    apt-get remove --purge -y && rm -rf /var/lib/apt/lists/*

# change default shell for build milvus
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# use gvm improved version, can support golang fast installation for x86 / arm platform
# maybe the milvus community can fork a version to provide a more stable service
# ref: https://soulteary.com/2021/12/15/golang-multi-version-management.html / https://soulteary.com/2022/05/12/better-golang-usage-on-m1-mac.html
RUN curl -sSL https://github.com/soulteary/gvm/raw/master/binscripts/gvm-installer | bash
ARG GO_BINARY_BASE_URL=https://golang.google.cn/dl/
ENV GO_BINARY_BASE_URL=${GO_BINARY_BASE_URL}
ENV GVM_ROOT=/root/.gvm
# keep same version with milvus builder images: milvus/build/docker/builder/cpu/ubuntu18.04/Dockerfile
ARG GOLANG_VERSION=1.16.9
ENV GOLANG_VERSION=${GOLANG_VERSION}
RUN source /root/.gvm/scripts/gvm && \
    gvm install go${GOLANG_VERSION} -B && \
    gvm use go${GOLANG_VERSION} --default
# after go1.15+, need set `GOROOT_BOOTSTRAP` for golang
ENV GOROOT_BOOTSTRAP=/root/.gvm
ENV GO111MODULE=on
ENV GOPATH="$HOME/go"
ENV PATH="$GOPATH/bin:$PATH"
ARG GOPROXY_URL=https://proxy.golang.org
ENV GOPROXY="${GOPROXY_URL},direct"



# download milvus source from github
# allow user specify a milvus git repository, branch, clone depth
ARG MILVUS_GIT_REPO=https://github.com/milvus-io/milvus.git
ENV MILVUS_GIT_REPO=${MILVUS_GIT_REPO}
ARG MILVUS_GIT_BRANCH=master
ENV MILVUS_GIT_BRANCH=${MILVUS_GIT_BRANCH}
ARG MILVUS_GIT_CLONE_DEPTH=
ENV MILVUS_GIT_CLONE_DEPTH=${MILVUS_GIT_CLONE_DEPTH}
RUN if [ "$MILVUS_GIT_CLONE_DEPTH" = "" ];then \
        bash -c "git clone ${MILVUS_GIT_REPO} --branch=${MILVUS_GIT_BRANCH}";\
    else \
        bash -c "git clone ${MILVUS_GIT_REPO} --branch=${MILVUS_GIT_BRANCH} --depth=${MILVUS_GIT_CLONE_DEPTH}"; \
    fi


WORKDIR /milvus
RUN source /root/.gvm/scripts/gvm && make milvus
