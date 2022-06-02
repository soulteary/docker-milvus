#!/usr/bin/env groovy

def gitShortCommit=''
def date=''
def image_tag=''

pipeline {

   agent {
        kubernetes {
            label 'ci'
            inheritFrom 'default'
            defaultContainer 'main'
            yamlFile 'ci/pod/build.yaml'
            customWorkspace '/home/jenkins/agent/workspace'
        }
   }
    environment {
        DOCKER_CREDENTIALS_ID = "f0aacc8e-33f2-458a-ba9e-2c44f431b4d2"
        TARGET_REPO = "milvusdb"
        CI_DOCKER_CREDENTIAL_ID = "ci-docker-registry"
        HARBOR_REPO = "registry.milvus.io"
    }
    stages {
        stage('Build & Publish Image') {
            steps{
            script {
            sh 'ls -lah '
            def date = sh(returnStdout: true, script: 'date +%Y%m%d').trim()
            def gitShortCommit = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
            def image_tag="${date}-${gitShortCommit}"
            sh """
            docker build -t registry.milvus.io/soulteary/milvus-base:${image_tag} -f docker/base/Dockerfile .
            """
            withCredentials([usernamePassword(credentialsId: "${env.CI_DOCKER_CREDENTIAL_ID}", usernameVariable: 'CI_REGISTRY_USERNAME', passwordVariable: 'CI_REGISTRY_PASSWORD')]){
                sh "docker login ${env.HARBOR_REPO} -u '${CI_REGISTRY_USERNAME}' -p '${CI_REGISTRY_PASSWORD}'"
            
                sh """
                    export MILVUS_HARBOR_IMAGE_REPO="registry.milvus.io/soulteary/milvus/milvus-base"
                    export MILVUS_IMAGE_TAG=${image_tag} 
                    docker push \${MILVUS_HARBOR_IMAGE_REPO}:\${MILVUS_IMAGE_TAG}
                    docker logout
                """
            }
            }
                
            }
        }
    }
}