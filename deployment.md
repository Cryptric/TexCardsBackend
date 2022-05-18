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
copy the ``docker-compose.yml`` to ``/opt/tex-cards/``

Make sure to fill in your email and a secure mysql password

## Start the application
Run ``cd /opt/tex-cards`` ``sudo docker-compose up -d`` to start the application in detached mode
For the first run, some services may fail their dependencies because of long setup delays, just run it again

Run ``sudo docker stop java web db traefik`` to stop the containers