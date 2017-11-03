#!/usr/bin/env groovy

def url = "http://git.cityos.com/yangqc/data-generator.git"
def credentialsId = "29bc5a5b-f645-4a65-a0fd-cdb8ffe8760d"

node('192.168.10.13') {
    stage 'checkout'
    git url: url, credentialsId: credentialsId

    stage 'build and push image'
    sh "mvn clean package -Dmaven.test.skip=true docker:build -DpushImage"
}

node('192.168.10.11') {
    stage 'checkout'
    git url: url, credentialsId: credentialsId

    stage 'run cityos-dg'
    sh "cd kubernetes && sh run_app.sh cityos-dg"
}
