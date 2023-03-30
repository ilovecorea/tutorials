# OAuth 2 Resource Server Sample
## Keycloak 실행
* tutorals/docker/docker-compose-keycloak.yaml 실행
* http://localhost:9999 접속 
* admin/P@ssw0rd 로그인 
* site1 realm 생성 
* app-cli 생성
  * Client authentication: off
  * Valid redirect URIs: http://localhost:8080/*
  * admin, user client role 생성 
* 일반 사용자 생성
  * Username: user
  * Email: user@example.com
  * Credentials
    * Password: password
    * Temporary: off
  * Role mapping: app-cli > user
* 관리자 사용자 생성
  * Username: admin
  * Email: admin@example.com
  * Credentials
    * Password: password
    * Temporary: off
  * Role mapping: app-cli > admin

