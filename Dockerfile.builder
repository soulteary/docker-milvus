FROM soulteary/milvus-base:ubuntu20.04-openblas3.9
LABEL maintainer=soulteary@gmail.com

# build cmake 3.16.3+ from source https://cmake.org
ENV DOCKER_CMAKE_TARBALL=https://cmake.org/files/v3.18/cmake-3.18.6-Linux-x86_64.tar.gz
RUN wget -qO- "${DOCKER_CMAKE_TARBALL}" | tar --strip-components=1 -xz -C /usr/local

# install linux build deps (from milvus/scripts/install_deps.sh)
RUN apt update && \
    apt-get install -y g++ gcc make lcov libtool m4 autoconf automake ccache libssl-dev zlib1g-dev libboost-regex-dev libboost-program-options-dev libboost-system-dev libboost-filesystem-dev libboost-serialization-dev python3-dev libboost-python-dev libcurl4-openssl-dev gfortran libtbb-dev \
    # install extra deps `clang-format-10 clang-tidy-10` (from milvus/build/docker/builder/cpu/ubuntu20.04/Dockerfile)
    clang-format-10 clang-tidy-10 && \
    # cleanup
    apt-get remove --purge -y && rm -rf /var/lib/apt/lists/*

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

