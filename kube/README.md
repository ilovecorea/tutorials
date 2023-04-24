## Jenkins 설치
jenkins/Dockerfile 생성 
```shell
# Set the base image for the new image
FROM jenkins/jenkins:lts
# sets the current user to root.
# This is necessary bc some of the following commands need root privileges.
USER root
# Update the package list on the image
RUN apt-get update && \
    # Install the packages for adding the Docker repo and installing Docker
    apt-get install -y apt-transport-https \
                       ca-certificates \
                       curl \
                       gnupg2 \
                       software-properties-common && \
    # Download the Docker repository key and add it to the system
    curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - && \
    # Add the Docker repository to the system
    add-apt-repository \
      "deb [arch=arm64] https://download.docker.com/linux/debian \
      $(lsb_release -cs) \
      stable" && \
    # Update the package list again to include the new repository
    apt-get update && \
    # Install the Docker CE package
    apt-get install -y docker-ce && \
    # Add the Jenkins user to the Docker group so the Jenkins user can run Docker commands
    usermod -aG docker jenkins
```

Dockerfile 빌드
```shell
docker build -t jenkins-arm64 .
```

docker-compose 작성
```shell
version: '3'

services:
  jenkins:
    image: myjenkins-arm64
    ports:
      - "18080:8080"
      - "50000:50000"
    volumes:
      - ~/jenkins:/var/jenkins_home
      - /var/run/docker.sock:/var/run/docker.sock
```

docker-compose 실행
```shell
docker-compose up
```

## Jenkins Plugin 설치
Maven Integration plugin
Pipeline Maven Integration Plugin
Docker Plugin
Docker Pipeline
Git Parameter Plugin

## Email 설정
Jenkins 관리 > Credentials > System > Global credentials 에서 New credentials 생성
* Kind: Username with password 
* Scope: Global 
* Username: ilovecorea@gmail.com 
* Password: ****
* ID: GMAIL
Jenkins 관리 > Configure System > Extended E-mail Notification 
* SMTP server: smtp.gmail.com
* SMTP Port: 465
* 고급 > Credentials: ilovecorea@gmail.com/*****
* 고급 > Use SSL: checked
* 고급 > Use TLS: checked

## Github 설정
* github Settings > Developer Settings > Personal access tokens > Tokens 에서 토큰 생성 
* jenkins 관리 > Credentials > System > Global credentials 에서 Net credentials 생성 
  * Scope: Global 
  * Secret: 생성한 엑서스 토큰 
  * ID: GITHUB 
* jenkins 관리 > Configure System > GibHub Server 
  * Name: GitHub 
  * Credentials: GITHUB 
  * Test connection: Credentials verified for user ilovecorea, rate limit: 4999

## JDK 설정

