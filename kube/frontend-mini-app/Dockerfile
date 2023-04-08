#FROM --platform=linux/amd64 nginx:1.17.1-alpine
FROM nginx:1.17.1-alpine
COPY ./nginx/nginx.conf /etc/nginx/nginx.conf
COPY ./angular-front/dist/angular-front /usr/share/nginx/html
EXPOSE 80
