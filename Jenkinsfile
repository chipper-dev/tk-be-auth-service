node{
    def app
    def name = "tk-be-api-gateway"
    def port = "9090"
    def network = "tk-be-network"
    def eurekaServer = "http://tk-be-api-gateway:8761"
    def build = "${env.BUILD_NUMBER}"
    def version = "1"
    def imageName = "chippermitrais/$name"
    def image =  imageName +':'+ version +'.'+ build
    def mvnHome = tool name: 'maven-default', type: 'maven'
    def mvnCMD = "${mvnHome}/bin/mvn"
    def remote = [:]
    remote.name = 'chippermitrais'
    remote.host = 'chippermitrais.ddns.net'
    remote.allowAnyHosts = true

    stage('SCM Checkout') {
        checkout scm
    }
    stage('Build Source Code') {
        sh "${mvnCMD} clean package -DskipTests"
    }
    stage('Build Docker Image') {
        app = docker.build(image)
    }
    stage('Push Image') {
        withCredentials([usernamePassword(credentialsId: 'team6-dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
            sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
            sh "docker push $image"
        }
    }
    stage('Remove Existing Docker Image & Run Application') {
        withCredentials([
            sshUserPrivateKey(credentialsId: 'team6-chippermitrais', keyFileVariable: 'sshkey', usernameVariable: 'sshuname')
            ]) {
                remote.user = env.sshuname
                remote.identityFile = env.sshkey
                def cmd = "docker ps -aqf name=$name"
                def container = sshCommand remote: remote, command: cmd

                if (container) {
                    echo 'Existing container found!!! Deleting...'
                    sshCommand remote: remote, command: "docker stop \$($cmd)"
                    sshCommand remote: remote, command: "docker rm \$($cmd)"
                    echo 'Done.'
                }

                sshCommand remote: remote, command: "docker images $imageName -q | xargs --no-run-if-empty docker rmi -f"

                sshCommand remote: remote, command: "docker run --name $name -p $port:$port --network $network -e EUREKA_SERVER_URL=$eurekaServer --restart always -d $image"
        }
    }
}