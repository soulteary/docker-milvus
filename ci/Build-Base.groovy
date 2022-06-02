#!/usr/bin/env groovy

def gitShortCommit=''
def date=''
def image_tag=''
def app_name='milvus-base'

pipeline {
   options{
    disableConcurrentBuilds(abortPrevious: true)
    // skipDefaultCheckout()
   }
   agent {
        kubernetes {
            label 'ci'
            inheritFrom 'default'
            defaultContainer 'main'
            yamlFile 'ci/pod/ci.yaml'
            customWorkspace '/home/jenkins/agent/workspace'
        }
   }
   environment{
      DOCKER_IMAGE="harbor-ap1.zilliz.cc/milvus/${app_name}"
      GITHUB_TOKEN_ID="github-token"
      GIT_REPO="${github}"
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
        stage('Build & Publish Image') {
            steps{
                container(name: 'kaniko',shell: '/busybox/sh') {
                  script {
                    sh 'ls -lah '
                    sh """
                    executor \
                    --context="`pwd`" \
                    --registry-mirror="nexus-nexus-repository-manager-docker-5000.nexus:5000"\
                    --insecure-registry="nexus-nexus-repository-manager-docker-5000.nexus:5000" \
                    --dockerfile "docker/base/Dockerfile" \
                    --destination=${DOCKER_IMAGE}:${image_tag}
                    """
                  }
                }
            }
        }
    }
}