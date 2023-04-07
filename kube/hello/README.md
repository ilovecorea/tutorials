## Docker build
```shell
docker build -t ilovecorea/hello .
```

## Docker Container Run
```shell
docker images
docker run -d -p 8100:8000 ilovecorea/hello
docker ps
docker exec -it 59fc9e9aa570 /bin/bash
```

## Docker Image Push
```shell
docker login
docker push ilovecorea/hello
```

## Install Minikube
```shell
brew install minikube
```

## Minikube Run
```shell
minikube start --driver=docker
```

## Install Kubectl
```shell
brew install kubectl && kubectl version
```

## Install Helm
```shell
brew install helm && helm version
```

## Install Jenkins
```shell
kubectl create namespace jenkins
helm repo add jenkins https://charts.jenkins.io
helm install jenkins jenkins/jenkins --version 4.3.10 --namespace jenkins
```
```shell
NAME: jenkins
LAST DEPLOYED: Thu Apr  6 15:45:46 2023
NAMESPACE: jenkins
STATUS: deployed
REVISION: 1
NOTES:
1. Get your 'admin' user password by running:
  kubectl exec --namespace jenkins -it svc/jenkins -c jenkins -- /bin/cat /run/secrets/additional/chart-admin-password && echo
2. Get the Jenkins URL to visit by running these commands in the same shell:
  echo http://127.0.0.1:8080
  kubectl --namespace jenkins port-forward svc/jenkins 8080:8080

3. Login with the password from step 1 and the username: admin
4. Configure security realm and authorization strategy
5. Use Jenkins Configuration as Code by specifying configScripts in your values.yaml file, see documentation: http://127.0.0.1:8080/configuration-as-code and examples: https://github.com/jenkinsci/configuration-as-code-plugin/tree/master/demos

For more information on running Jenkins on Kubernetes, visit:
https://cloud.google.com/solutions/jenkins-on-container-engine

For more information about Jenkins Configuration as Code, visit:
https://jenkins.io/projects/jcasc/


NOTE: Consider using a custom image with pre-installed plugins
```

## Install Argocd
```shell
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

## Connect argocd
```shell
kubectl get all -n argocd
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d; echo
kubectl port-forward svc/argocd-server -n argocd 8082:443
```

## Jenkins SSH Key Pair
ssh 키 생성
```shell
kubectl exec --namespace jenkins -it svc/jenkins -c jenkins -- bash
```
https://github.com/settings/keys SSH Key 등록
private key 조회
```shell
cat /var/jenkins_home/.ssh/id_rsa
```
'Jenkins 관리' > 'Manage Credentials' 메뉴로 이동 후 'Store scoped to Jenkins' 항목에서 아래와 같이 Jenkins Store에 새로운 Credential을 추가