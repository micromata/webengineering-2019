# Overview

This is the backend for our application. We will use [https://start.spring.io/](Spring Boot Initializr) to create a skelton for our backend.

# Persistent database

Start a [postgres](https://hub.docker.com/_/postgres) container in docker using 

    docker run --rm --name backend-postgres -e POSTGRES_PASSWORD=docker -p 5433:5432 \
        -v $(pwd)/database:/var/lib/postgresql/data postgres     
        
Note that you have to adapt the command line for windows. Be aware that we use the non-standard postgres port `5433` 
instead of 5432 to prevent port conflicts with a possible already running postgres database on your system.

Use `-d` as parameter to start in daemon mode without logs.

Note that you can also start a database using docker-compose using `docker-compose up` in the `backend` directory.         
