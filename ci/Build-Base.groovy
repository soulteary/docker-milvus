#!/usr/bin/env groovy

def gitShortCommit=''
def date=''
def image_tag=''


pipeline {
  //  options{
  //   disableConcurrentBuilds(abortPrevious: true)
  //   // skipDefaultCheckout()
  //  }
   agent {
        kubernetes {
            label 'ci-registry-a'
            inheritFrom 'default'
            defaultContainer 'main'
            yamlFile 'ci/pod/ci.yaml'
            customWorkspace '/home/jenkins/agent/workspace'
        }
   }
   environment{
      DOCKER_BASE_IMAGE="registry.milvus.io/milvus/milvus-base"
      DOCKER_BUILDER_IMAGE="registry.milvus.io/milvus/milvus-builder"
      DOCKER_APP_IMAGE="registry.milvus.io/milvus/milvus-app"
      GITHUB_TOKEN_ID="github-token"
      GIT_REPO="${github}"
   }

    parameters{
        string(
            description: 'Repo',
            name: 'repo',
            defaultValue: 'https://github.com/jaime0815/milvus.git'
        )
        string(
            description: 'Branch',
            name: 'branch',
            defaultValue: 'huawei-debug'
        )
    }
    stages {
        stage('Checkout'){
            steps{
                script {
                    sh 'printenv'
                    date = sh(returnStdout: true, script: 'date +%Y%m%d').trim()
                    gitShortCommit = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    image_tag="${date}-${gitShortCommit}"
                }
            }
        }
        // stage('Build & Publish Base Image') {
        //     steps{
        //         container(name: 'kaniko',shell: '/busybox/sh') {
        //           script {
        //             sh 'ls -lah '
        //             sh """
        //             executor \
        //             --context="`pwd`" \
        //             --cache=true \
        //             --cache-ttl=24h \
        //             --registry-mirror="nexus-nexus-repository-manager-docker-5000.nexus:5000"\
        //             --insecure-registry="nexus-nexus-repository-manager-docker-5000.nexus:5000" \
        //             --dockerfile "docker/base/Dockerfile" \
        //             --destination=${DOCKER_BASE_IMAGE}:${image_tag}
        //             """
        //           }
        //         }
        //     }
        // }
        stage('Build & Publish Builder Image') {
            steps{
                container(name: 'kaniko',shell: '/busybox/sh') {
                  script {
                    sh 'ls -lah '
                    sh """
                    executor \
                    --cache=true \
                    --cache-ttl=24h \
                    --context="`pwd`" \
                    --registry-mirror="nexus-nexus-repository-manager-docker-5000.nexus:5000"\
                    --insecure-registry="nexus-nexus-repository-manager-docker-5000.nexus:5000" \
                    --build-arg=BASE_IMAGE=registry.milvus.io/milvus/milvus-base:20220602-b828e22 \
                    --build-arg=MILVUS_GIT_REPO=${params.repo} \
                    --build-arg=MILVUS_GIT_BRANCH=${params.branch}\
                    --build-arg=MILVUS_GIT_COMMIT=${gitShortCommit} \
                    --dockerfile "docker/builder/Dockerfile" \
                    --destination=${DOCKER_BUILDER_IMAGE}:${image_tag}
                    """
                  }
                }
            }
        }
        stage('Build & Publish App Image') {
            steps{
                container(name: 'kaniko',shell: '/busybox/sh') {
                  script {
                    sh "echo ${DOCKER_APP_IMAGE}:${image_tag}"
                    sh """
                    executor \
                    --cache=true \
                    --cache-ttl=24h \
                    --context="`pwd`" \
                    --registry-mirror="nexus-nexus-repository-manager-docker-5000.nexus:5000"\
                    --insecure-registry="nexus-nexus-repository-manager-docker-5000.nexus:5000" \
                    --build-arg=BUILDER_IMAGE=${DOCKER_BUILDER_IMAGE}:${image_tag} \
                    --dockerfile "docker/app/Dockerfile" \
                    --destination=${DOCKER_APP_IMAGE}:${image_tag}
                    """
                  }
                }
            }
        }
    }
}