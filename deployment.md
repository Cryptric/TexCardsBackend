# Deployment
Deployment steps to deploy the tex-cards stack

## Requirements
``Docker version 20.10.14, build a224086``
``docker-compose version 1.29.2, build 5becea4c``

## File hierarchy setup
Setup for usage with domain tex-cards.cryptric.ch

``cd /opt`` \
``sudo mkdir tex-cards`` \
``cd tex-cards`` \
``sudo mkdir java-data nginx traefik`` \
``sudo touch traefik/acme.json`` \
``sudo chmod 600 traefik -R`` \
``sudo mkdir nginx/conf nginx/html`` \

## File/config setup
``sudo nano nginx/conf/tex-cards.cryptric.ch.conf`` \
Create the file with content:
```
server {
    listen 80;
    listen [::]:80;

    server_name tex-cards.cryptric.ch www.tex-cards.cryptric.ch;

    root /var/www/tex-cards.cryptric.ch;

    index index.html;

    try_files $uri /index.html;

}
```
Copy the backend jar file named ``tex-cards-rest-service-0.0.1-SNAPSHOT.jar`` into ``/opt/tex-cards/java-data/``

Copy index.html and the other files for the frontend into ``/opt/tex-cards/nginx/html/``

Create the ``docker-compose.yml`` file \
``cd /opt/tex-cards`` \
``sudo nano docker-compose.yml`` \
Create the file with content:
```
version: '3.1'
services:
  db:
    container_name: db
    image: mysql
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: tex_cards
    volumes:
      - /opt/tex-cards/mysql-data:/var/lib/mysql
    labels:
      - "traefik.enable=false"

  traefik:
    container_name: traefik
    image: traefik:2.6.0
    restart: unless-stopped
    ports:
      - 443:443
      - 8080:8080
    command:
#     - --log.level=DEBUG
      - --entrypoints.web-secure.address=:443
      - --entrypoints.webalt.address=:8080
      - --certificatesresolvers.le.acme.email=[EMAIL]
      - --certificatesresolvers.le.acme.storage=/acme.json
      - --certificatesresolvers.le.acme.tlschallenge=true
      - --providers.docker=true
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
      - /opt/tex-cards/traefik/acme.json:/acme.json
    labels:
      - "traefik.enable=false"

  java:
    container_name: java
    image: openjdk:17.0.2
    restart: unless-stopped
    depends_on:
      - db
    volumes:
      - /opt/tex-cards/java-data:/usr/jar
    working_dir: /usr/jar
    command: java -jar tex-cards-rest-service-0.0.1-SNAPSHOT.jar
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.java.rule=Host(`tex-cards.cryptric.ch`)"
      - "traefik.http.routers.java.tls.certresolver=le"
      - "traefik.http.services.java.loadbalancer.server.port=8080"
      - "traefik.http.routers.java.entrypoints=webalt"

  web:
    container_name: web
    image: nginx:latest
    volumes:
      - /opt/tex-cards/nginx/html:/var/www/tex-cards.cryptric.ch/
      - /opt/tex-cards/nginx/conf/:/etc/nginx/conf.d/
    restart: unless-stopped
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.web.rule=Host(`tex-cards.cryptric.ch`)"
      - "traefik.http.routers.web.tls.certresolver=le"
      - "traefik.http.services.web.loadbalancer.server.port=80"
      - "traefik.http.routers.web.entrypoints=web-secure"
```
Make sure to fill in your email and a secure mysql password

## Start the application
Run ``cd /opt/tex-cards`` ``sudo docker-compose up -d`` to start the application in detached mode
For the first run, some services may fail their dependencies because of long setup delays, just run it again

Run ``sudo docker stop java web db traefik`` to stop the containers